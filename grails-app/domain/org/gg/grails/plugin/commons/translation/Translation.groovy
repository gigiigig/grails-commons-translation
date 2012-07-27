package org.gg.grails.plugin.commons.translation

class Translation {
    
    String text
    String language // annotazione a 2 lettere
	
    static constraints = {
		sort:"language"
    }
	
	static mapping = {
		text type:'text'
	}
}
 