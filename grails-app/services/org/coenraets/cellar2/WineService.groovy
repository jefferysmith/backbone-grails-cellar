package org.coenraets.cellar2

class WineService {

    def findAll() {
        Wine.findAll()
    }

    def findByName(name) {
        Wine.findByName(name)
    }
}
