package org.gg.grails.plugin.commons.translation

class TranslationTagLib {

	/**
	 * Field that genearte input form for multilanguage field
	 */
	def translationField = { attrs, body ->
		createField("text",attrs, body)
	}

	/**
	 * Field that genearte input form for multilanguage text area
	 */
	def translationArea = { attrs, body ->
		createField("area",attrs, body)
	}

	def createField(type,attrs, body){

		def bean = attrs.bean
		def field = attrs.field

		def langs = session.langs
		def text = ""

		langs.each{ c->

			def value = bean."${field}Translations".find{it.language == c}?.text ?:""

			out << "<div class=\"${c}\">"+"\n";

			if(type == "text"){
				out << g.textField(name: "${field}.${c}", 'class': "${c}" ,value:"${value}") +"\n"
			}else{
				out << g.textArea(name: "${field}.${c}", 'class': "${c}" ,value:"${value}") +"\n"
			}

			out << message(code: "${c}.label",default : "${c}") +"\n"
			out << "</div>" +"\n"
		}
	}
}
