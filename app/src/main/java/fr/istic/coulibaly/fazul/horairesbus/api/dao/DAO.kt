package fr.istic.coulibaly.fazul.horairesbus.api.dao


interface DAO {
   fun download()
   fun refresh()
   fun latest()

}