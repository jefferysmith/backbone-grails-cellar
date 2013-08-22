import org.codehaus.groovy.grails.support.proxy.DefaultProxyHandler
import org.coenraets.cellar2.CustomDomainJSONMarshaller
import org.coenraets.cellar2.Wine

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        // Register a customer json marshaller
        grails.converters.JSON.registerObjectMarshaller(new CustomDomainJSONMarshaller(false, new DefaultProxyHandler(), grailsApplication))

        // map a value of "null" for a json attribute to false
        // fixed in future version http://jira.grails.org/browse/GRAILS-7739
        org.codehaus.groovy.grails.web.json.JSONObject.NULL.metaClass.asBoolean = {-> false}
        environments {
            development {
                loadWines()
            }
        }
    }
    def destroy = {
    }

    void loadWines() {
        if(!Wine.findByName("CHATEAU DE SAINT COSME")) {
            println "Empty database, creating wines."
            new Wine( name: 'CHATEAU DE SAINT COSME',
                      grapes: 'Grenache / Syrah',
                      country: 'France',
                      region: 'Southern Rhone / Gigondas',
                      year: '2009',
                      picture: 'saint_cosme.jpg',
                      description: 'The aromas of fruit and spice give one a hint of the light drinkability of this lovely wine, which makes an excellent complement to fish dishes.' ).save()
            new Wine( name: 'LAN RIOJA CRIANZA',
                      grapes: 'Tempranillo',
                      country: 'Spain',
                      region: 'Rioja',
                      year: '2006',
                      picture: 'lan_rioja.jpg',
                      description: 'A resurgence of interest in boutique vineyards has opened the door for this excellent foray into the dessert wine market. Light and bouncy, with a hint of black truffle, this wine will not fail to tickle the taste buds.' ).save()
            new Wine( name: 'MARGERUM SYBARITE',
                      grapes: 'Sauvignon Blanc',
                      country: 'USA',
                      region: 'California Central Coast',
                      year: '2010',
                      picture: 'margerum.jpg',
                      description: 'The cache of a fine Cabernet in ones wine cellar can now be replaced with a childishly playful wine bubbling over with tempting tastes of\nblack cherry and licorice. This is a taste sure to transport you back in time.' ).save()
        }
    }
}
