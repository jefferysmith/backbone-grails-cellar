package org.coenraets.cellar2

class WineController {

    def index = {
        redirect(action:list)
    }

    def list = {
        render "<h2>List</h2><p>some text here</p>"
    }
}
