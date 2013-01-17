grails-commons-translation
==========================

Plugin to manage fields translations on Grails

This plugin allow to create multilanguage fields in Grails.

To add translation filed to a domain class, in domain class import Translation class

    org.gg.grails.plugin.commons.translation
  
After add for every mylti language field,
one transient field with the name you want, and one hasMany relation
with the name FIELD_NAMETranslations,

so for example :  "title":

    def title
    static hasMany = [
        titleTranslations : Translations
    ]

now for save tranlation on domain instance, you must use "translationService", 
inside a controller or directly in the domain class.
In both case, you have to call translationService before validation.
For instance in controller, you can have:

    translationService.add(domainInstance,'title')
        if (!domainInstance.hasErrors() && domainInstance.save(flush: true)) { ..
    }
  
In domain class you can call service in the beforeValidation hook 

    def beforeValidate() {
        translationService.add(this,'title')
    }

add method at this time, verify if at lest one of field translations have value,
otherwise invalidate domain class, addin error 'default.blank.message' to transient field, "title" in this case

add method have also optional paramenter leght , 

    translationService.add(this,'title',34) 
  
wich control i text is less long than leght,
else add error  'default.maxsize.message' to field


