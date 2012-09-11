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
      render "<h2>Save method</h2>"
      if(params.id) {
        render "<h3>error: no params supported</h3>"
      } else {
        render "<h3>add a new wine</h3>"
      }
    }

    /**
     * PUT api/wines/{id} : update wine with id
     */
     def update = {
       render "<h2>Update method</h2>"
       if(params.id) {
         render "<h3>update wine with id: ${params.id}</h3>"
       } else {
         render "<h3>error: param required</h3>"
       }
     }

    /**
     * DELETE api/wines/{id} : delete wine with id
     */
     def delete = {
       render "<h2>Delete method</h2>"
       if(params.id) {
         render "<h3>delete wine with id: ${params.id}</h3>"
       } else {
         render "<h3>error: param required</h3>"
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
