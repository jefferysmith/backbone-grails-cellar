package org.coenraets.cellar2

import grails.converters.JSON

class WineController {

    WineService wineService

    /**
     * GET api/wines : list of all wines
     * GET api/wines/{id} : get wine with id
     */
    def show = {
        if (params.id) {
            log.debug("findById: ${params.id}")
            render wineService.findById(params.id) as JSON
        } else {
            log.debug("findAll")
            render wineService.findAll() as JSON
        }
    }

    /**
     * POST api/wines : add a new wine
     */
    def save = {
        if(params.id) {
            log.error("cannot create a new wine from an existing wine - id: ${params.id}")
            render "ERROR"
        } else {
            log.debug("creating new wine")
            // not sure if this approach works with relations/nested objects
            def wine = new Wine(params)
            render wineService.create(wine) as JSON
        }
    }

    /**
     * PUT api/wines/{id} : update wine with id
     */
    def update = {
        if(params.id) {
            log.debug("update wine with id: ${params.id}")
            def updatedWine = wineService.findById(params.id)
            if(!updatedWine) {
                log.debug("Unable to find wine with id: ${params.id}")
                render "ERROR"
            } else {
                //TODO: address any security concerns with this data-binding:
                // http://grails.org/doc/2.1.0/guide/single.html#dataBinding
                updatedWine.properties = params
                render wineService.update(updatedWine) as JSON
            }
        } else {
            log.error("cannot update a new wine")
            render "ERROR"
        }
    }

    /**
     * DELETE api/wines/{id} : delete wine with id
     */
    def delete = {
        if(params.id) {
            log.debug("delete wine with id: ${params.id}")
            wineService.remove(params.id)
            render "OK"
        } else {
            log.debug("cannot delete wine without providing an id")
            render "ERROR"
        }
    }

    /**
     * GET api/wines/search/{query} : search wines for query term
     */
    def search = {
        render "<h2>Search method</h2>"
        if(params.query) {
            render "<h3>search wines for : ${params.query}</h3>"
        } else {
            render "<h3>error: param required</h3>"
        }
    }
}
