package org.coenraets.cellar2

import groovy.transform.ToString

@ToString(includeNames=true)
class Wine {

    String name
    String grapes
    String country
    String region
    String year
    String picture
    String description

    static constraints = {
    }
}
