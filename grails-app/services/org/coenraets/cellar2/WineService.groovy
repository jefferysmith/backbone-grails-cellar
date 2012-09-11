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

    def update(wine) {
        if(!wine.id) {
            log.error("Can't update an unsaved wine. Use create instead")
            throw new IllegalArgumentException()
        }
        wine.save()
    }

    def remove(id) {
        def wine = Wine.get(id)
        if (!wine) {
            log.error("Unable to delete wine with id: ${id}. Wine not found")
            throw new IllegalArgumentException()
        }
        wine.delete(flush: true)
    }
}
