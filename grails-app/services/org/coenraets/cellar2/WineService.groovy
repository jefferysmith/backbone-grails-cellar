package org.coenraets.cellar2

class WineService {

    def findAll() {
        Wine.findAll()
    }

    def findByName(name) {
        Wine.findByName(name)
    }

    def findById(id) {
        Wine.get(id)
    }

    def create(wine) {
        if(wine.id) {
            log.error("Can't create new wine using existing wine. Given wine with id ${wine.id}")
            throw new IllegalArgumentException()
        }
        wine.save()
    }
}
