package org.coenraets.cellar2

import grails.converters.JSON
import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.commons.*
import org.codehaus.groovy.grails.support.proxy.EntityProxyHandler
import org.codehaus.groovy.grails.support.proxy.ProxyHandler
import org.codehaus.groovy.grails.web.converters.ConverterUtil
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller
import org.codehaus.groovy.grails.web.json.JSONWriter
import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl

/**
 * Modified version of org.codehaus.groovy.grails.web.converters.marshaller.json.DomainClassMarshaller
 *
 * See http://jira.grails.org/browse/GRAILS-5791 for discussion
 *
 * Reads static field "serialize" and serializes the domain object accordingly. Supports
 * field exclusion and rename.
 *
 * Example:
 *
 * class Trip {
 *      static serialize = {
 *          user name:'username' // field name will be serialized as 'username' in json
 *          password exclude:true // don't serialize the adminPasscode field
 *          //_class_ exclude:true // for the implict class field.
 *      }
 *  }
 *
 *  Use configuration variable grails.json.serialization.exclude to configure global excludes
 *
 *  Example:
 *
 *  grails.json.serialization.exclude = ["class", "metaClass", "attached"]
 */
public class CustomDomainJSONMarshaller implements ObjectMarshaller<JSON> {
    private static final String PROPERTY_NAME = "serialize";

    private GrailsApplication application;
    private ProxyHandler proxyHandler;
    private String baseUri

    // [ class : [ excludedFields]]
    private Map<Class<?>, Set<String>> domainSerializeExcludeCache

    // [ class : [oldFieldName : newFieldName]]
    private Map<Class<?>, Map<String, String>> domainSerializeRenamedFieldCache

    private boolean includeVersion = false

    public CustomDomainJSONMarshaller(boolean includeVersion, ProxyHandler proxyHandler, GrailsApplication app) {
        this.application = app;
        this.proxyHandler = proxyHandler
        this.includeVersion = includeVersion
        domainSerializeExcludeCache = new HashMap<Class<?>, Set<String>>()
        domainSerializeRenamedFieldCache = new HashMap<Class<?>, Map<String,String>>()

        baseUri = app.config.grails.serverURL

        log.debug("CustomDomainJSONMarshaller instantiated");
    }

    public boolean supports(Object object) {
        String name = ConverterUtil.trimProxySuffix(object.getClass().getName());
        return application.isArtefactOfType(DomainClassArtefactHandler.TYPE, name);
    }

    public void marshalObject(Object value, JSON json) throws ConverterException {
        JSONWriter writer = json.getWriter();
        try {
            writer.object();

            Class<?> targetClass = value.getClass();
            evaluateDomainSerializeSettings(targetClass)

            GrailsDomainClass domainClass = (GrailsDomainClass) application.getArtefact(DomainClassArtefactHandler.TYPE,
                    ConverterUtil.trimProxySuffix(targetClass.name));
            BeanWrapper beanWrapper = new BeanWrapperImpl(value);

            if (!isPropertyExcluded(targetClass, "_class_") && !isPropertyExcluded(targetClass, "class")) {
                writer.key("class").value(domainClass.clazz.name);
            }
            // "id"
            GrailsDomainClassProperty id = domainClass.identifier;
            Object idValue = beanWrapper.getPropertyValue(id.name);
            json.property("id", idValue);
            writer.key("uri").value(getUri(idValue, domainClass))

            if (includeVersion) {
                GrailsDomainClassProperty versionProperty = domainClass.getVersion();
                Object version = beanWrapper.getPropertyValue(versionProperty.name);
                json.property("version", version);
            }

            GrailsDomainClassProperty[] properties = domainClass.getPersistentProperties();

            for (GrailsDomainClassProperty property : properties) {

                // check exclusion settings
                if (isPropertyExcluded(targetClass, property.name)) {
                    continue;
                }

                writer.key(propertyName(targetClass, property.name));

                if (!property.isAssociation()) {
                    // Write non-relation property
                    Object val = beanWrapper.getPropertyValue(property.name);
                    json.convertAnother(val);
                } else {
                    Object referenceObject = beanWrapper.getPropertyValue(property.name);
                    if (isRenderDomainClassRelations()) {
                        if (referenceObject == null) {
                            writer.value(null);
                        } else {
                            referenceObject = proxyHandler.unwrapIfProxy(referenceObject);
                            if (referenceObject instanceof SortedMap) {
                                referenceObject = new TreeMap((SortedMap) referenceObject);
                            } else if (referenceObject instanceof SortedSet) {
                                referenceObject = new TreeSet((SortedSet) referenceObject);
                            } else if (referenceObject instanceof Set) {
                                referenceObject = new HashSet((Set) referenceObject);
                            } else if (referenceObject instanceof Map) {
                                referenceObject = new HashMap((Map) referenceObject);
                            } else if (referenceObject instanceof Collection) {
                                referenceObject = new ArrayList((Collection) referenceObject);
                            }
                            json.convertAnother(referenceObject);
                        }
                    } else {
                        if (referenceObject == null) {
                            json.value(null);
                        } else {
                            GrailsDomainClass referencedDomainClass = property.getReferencedDomainClass();

                            // Embedded are now always fully rendered
                            if (referencedDomainClass == null || property.isEmbedded()
                            || GrailsClassUtils.isJdk5Enum(property.getType())) {
                                json.convertAnother(referenceObject);
                            } else if (property.isOneToOne() || property.isManyToOne() || property.isEmbedded()) {
                                asShortObject(referenceObject, json, referencedDomainClass.getIdentifier(),
                                        referencedDomainClass);
                            } else {
                                GrailsDomainClassProperty referencedIdProperty = referencedDomainClass.getIdentifier();
                                String refPropertyName = referencedDomainClass.getPropertyName();
                                if (referenceObject instanceof Collection) {
                                    Collection o = (Collection) referenceObject;
                                    writer.array();
                                    for (Object el : o) {
                                        asShortObject(el, json, referencedIdProperty, referencedDomainClass);
                                    }
                                    writer.endArray();
                                } else if (referenceObject instanceof Map) {
                                    Map<Object, Object> map = (Map<Object, Object>) referenceObject;
                                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                                        String key = String.valueOf(entry.getKey());
                                        Object o = entry.getValue();
                                        writer.object();
                                        writer.key(key);
                                        asShortObject(o, json, referencedIdProperty, referencedDomainClass);
                                        writer.endObject();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            writer.endObject();
        } catch (Exception e) {
            throw new ConverterException("Exception in CustomDomainMarshaller", e);
        }
    }

    private String propertyName(Class<?> clazz, String propertyName) {
        Map<String, String> renamedFields = domainSerializeRenamedFieldCache.get(clazz)
        if (renamedFields.containsKey(propertyName)) {
            return renamedFields.get(propertyName)
        }
        return propertyName
    }

    private boolean isPropertyExcluded(Class<?> clazz, String propertyName) {
        Set<String> excluded = domainSerializeExcludeCache.get(clazz)
        excluded.contains(propertyName)
    }

    private void evaluateDomainSerializeSettings(Class<?> domainClass) {
        initializeDataStructures(domainClass)
        Set<String> excluded = domainSerializeExcludeCache.get(domainClass)
        Map<String, String> renamed = domainSerializeRenamedFieldCache.get(domainClass)

        // [fieldName : [setting : value]]
        Map<String, Map<Object, Object>> domainSettings = evaluateStaticSerializeField(domainClass)

        for (Map.Entry<String, Map<Object, Object>> fieldSettingsEntry : domainSettings.entrySet()) {
            String fieldName = fieldSettingsEntry.key;
            Map<Object, Object> fieldSettings = fieldSettingsEntry.value;
            if (fieldSettings.containsKey("exclude")) {
                excluded.add(fieldName);
            } else if (fieldSettings.containsKey("name")) {
                renamed.put(fieldName, fieldSettings.get("name"))
            }
        }

        def exclude = application.config.grails.json.serialization.exclude
        excluded.addAll(exclude)
    }

    /**
     *
     * @return [fieldname : [setting: value]]
     */
    private Map<String, Map<Object, Object>> evaluateStaticSerializeField(Class<? extends Object> domainClass) {
        SerializeSettingsBuilder delegate = new SerializeSettingsBuilder(domainClass);

        LinkedList<Class<?>> classChain = getSuperClassChain(domainClass);
        // Evaluate all the constraints closures in the inheritance chain
        for (Class<?> clazz : classChain) {
            Closure<?> c = (Closure<?>) GrailsClassUtils.getStaticFieldValue(domainClass, PROPERTY_NAME);
            if (c != null) {
                c = (Closure<?>) c.clone();
                c.setResolveStrategy(Closure.DELEGATE_ONLY);
                c.setDelegate(delegate);
                c.call();
            } else {
                log.debug("User-defined serialize rules not found on class [" + clazz
                        + "], applying default serialize rules");
            }
        }

        return delegate.buildResult
    }

    private void initializeDataStructures(Class<? extends Object> domainClass) {
        Set<String> excluded = domainSerializeExcludeCache.get(domainClass)
        Map<String, String> renamed = domainSerializeRenamedFieldCache.get(domainClass)

        if (excluded == null) {
            domainSerializeExcludeCache.put(domainClass, new HashSet<String>())
        }

        if (renamed == null) {
            domainSerializeRenamedFieldCache.put(domainClass, new HashMap<String, String>())
        }
    }

    public class SerializeSettingsBuilder extends BuilderSupport {

        private Class<?> targetClass;

        // Result from parsing the serialize-field
        private Map<String, Map<Object, Object>> serializeSettings;

        public SerializeSettingsBuilder(Class<?> theClass) {
            this.targetClass = theClass;
            serializeSettings = new HashMap<String, Map<Object, Object>>();
        }

        public Map<String, Map<Object, Object>> getBuildResult() {
            return serializeSettings;
        }

        @Override
        protected Object createNode(Object name) {
            return null;
        }

        @Override
        protected Object createNode(Object name, Object value) {
            return null;
        }

        @Override
        protected Object createNode(Object name, Map attributes) {
            Map fieldSettings = serializeSettings.get(name);
            if (fieldSettings == null) {
                fieldSettings = new HashMap();
                serializeSettings.put(name.toString(), fieldSettings);
            }
            fieldSettings.putAll(attributes);

            return serializeSettings;
        }

        @Override
        protected Object createNode(Object name, Map attributes, Object value) {
            return null;
        }

        @Override
        protected void setParent(Object parent, Object child) {
        }
    }

    private static LinkedList<Class<?>> getSuperClassChain(Class<?> theClass) {
        LinkedList<Class<?>> classChain = new LinkedList<Class<?>>();
        Class<?> clazz = theClass;
        while (clazz != Object.class && clazz != null) {
            classChain.addFirst(clazz);
            clazz = clazz.getSuperclass();
        }
        return classChain;
    }

    protected boolean isRenderDomainClassRelations() {
        return false;
    }

    protected void asShortObject(Object refObj, JSON json, GrailsDomainClassProperty idProperty,
    GrailsDomainClass referencedDomainClass) throws ConverterException {
        Object idValue = getId(refObj, idProperty)

        JSONWriter writer = json.getWriter();
        writer.object();
        writer.key("class").value(referencedDomainClass.getName());
        writer.key("id").value(idValue);
        writer.key("uri").value(getUri(idValue, referencedDomainClass))
        writer.endObject();
    }

    private String getUri(Object idValue, GrailsDomainClass referencedDomainClass) {
        def className = GrailsNameUtils.getPropertyName(referencedDomainClass.shortName)
        "$baseUri/$className/$idValue"
    }

    private Object getId(refObj, GrailsDomainClassProperty idProperty) {
        Object idValue;

        if (proxyHandler instanceof EntityProxyHandler) {
            idValue = ((EntityProxyHandler) proxyHandler).getProxyIdentifier(refObj);
            if (idValue == null) {
                idValue = new BeanWrapperImpl(refObj).getPropertyValue(idProperty.getName());
            }
        } else {
            idValue = new BeanWrapperImpl(refObj).getPropertyValue(idProperty.getName());
        }
        idValue
    }
}