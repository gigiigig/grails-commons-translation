package org.gg.grails.plugin.commons.translation

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.*



class TranslationService {

	def grailsApplication

	def add(bean,field) {
		add(bean,field,-1)
	}

	def add(bean,field,length) {

		def hasLenght = length > -1
		
		def isNames = false
		def isTooLong = false

		def session = RequestContextHolder.currentRequestAttributes().getSession()
		def params = RequestContextHolder.currentRequestAttributes().getParams()

		log.debug "add [" + RequestContextHolder.currentRequestAttributes().class + "]"

		//if list is null create list
		if(bean."${field}Translations" == null)
			bean."${field}Translations" = []

		session.langs.each {

			def translation

			//cerco il paramatro nella get
			if(params["${field}.${it}"]){

				//verifico se serve la dimensione
				if(hasLenght){
					
					log.debug "add text legth[" + params["${field}.${it}"].length() + "]"
					
					if(params["${field}.${it}"].length() > length){
						isTooLong = true;
					}
				}

				//creo la traduzione
				translation = new Translation(language: "${it}",text: params["${field}.${it}"])

				//setto a true perche so che la variabile è valida
				isNames = isNames || true

			}else{

				//se non c'è nei parametri creo la lingua vuota
				translation = new Translation(language: "${it}",text: "")
				isNames = isNames || false

			}

			//cerco la traduzione in quelle già presenti nell'oggeti
			def currentTranslation = bean."${field}Translations".find{ cl ->
				cl.language == it
			}

			log.debug "add current tranlation[" + currentTranslation + "]"

			//se c'è la aggiorno
			if(currentTranslation){
				currentTranslation.text = translation.text
			}else{
				//sennò la ggiungo alla lista
				bean."${field}Translations".add(translation)
			}

		}


		//add ather errors
		//		dishInstance.validate()

		//se non ci sono valori per la variabile dsi nessuna
		//lingua rejecto il field

		def beanClassName = bean.class.simpleName.toLowerCase()
		def fieldCapitalized = field.capitalize() as String

		log.debug "add [" + "${beanClassName}.${field}.label" + "]"

		fieldCapitalized = grailsApplication.getMainContext().
				getMessage("${beanClassName}.${field}.label".toString() ,
				[fieldCapitalized]as Object[]
				,fieldCapitalized,Locale.getDefault())

		if(!isNames){

			bean.errors.rejectValue("${field}", "default.blank.message",
					[fieldCapitalized]as Object[] ,"")

		}
		
		if(isTooLong){
			bean.errors.rejectValue("${field}", "default.maxsize.message","${field.capitalize()} can be max ${length} charaters long")
		}

	}
}
