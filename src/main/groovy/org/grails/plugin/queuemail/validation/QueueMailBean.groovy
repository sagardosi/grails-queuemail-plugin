package org.grails.plugin.queuemail.validation

import grails.util.Holders
import grails.validation.Validateable
import org.grails.plugin.queuemail.EmailQueue
import org.grails.plugin.queuemail.enums.QueueStatus
import org.grails.plugin.queuemail.enums.SearchTypes

import static org.grails.plugin.queuemail.enums.SearchTypes.USER


class QueueMailBean implements Validateable {

	def queueMailUserService = Holders.grailsApplication.mainContext.getBean('queueMailUserService')


	Long userId

	SearchTypes searchBy
	String searchFor
	Long userSearchId
	boolean hideUsers=true
	QueueStatus status

	Integer max=Math.min(10, 50)
	Integer offset
	String sort
	String position
	String order='desc'

	String deleteBy
	boolean safeDel=false

	boolean superUser=false
	boolean jobControl=false

	static constraints = {
		status (inList:EmailQueue.REPORT_STATUS_ALL)
		userSearchId(nullable:true)
		searchFor(nullable:true)
		searchBy(inList:QueueMailLists.SEARCH_TYPES)
		deleteBy(nullable:true, inList:QueueMailLists.deleteList)
	}

	/**
	 * Work out if userId is superUser
	 * Rather expensive but paying the price
	 * for userBase separation
	 * @return
	 */
	boolean getSuperUser() {
		return queueMailUserService.isSuperUser(userId)
	}

	/**
	 * returns correct search select listing
	 * based on privileges
	 * @return
	 */
	List getSearchList() {
		return (getSuperUser() ? QueueMailLists.SEARCH_TYPES: QueueMailLists.SEARCH_TYPES-[USER])
	}

	/**
	 * return correct status based on user privileges
	 * @return
	 */
	List getStatuses() {
		return (getSuperUser() ? EmailQueue.REPORT_STATUS_ALL : EmailQueue.REPORT_STATUS)
	}

	List getAdminButtons() {
		return (getSuperUser() ? QueueMailLists.CHANGE_TYPES : [])
	}
	/**
	 * When the user searches for a username on the front end listing
	 * Since this plugin has been designed without the awareness of how you run your userbase.
	 * It calls on queueMailUserService.getRealUserId(searchBy) which will return a userId bound
	 * to search username.
	 *
	 * At the moment provided service returns 1 the same as currentuser which is also 1
	 *
	 * The only thing you need to override is queueMailUserService and make your own then using
	 * resources.groovy bind it back to be actually queueMailUserService so plugin uses your version instead
	 * to return a real userid
	 *
	 * @return
	 */
	Long getUserSearchId() {
		if (searchFor==USER && superUser) {
			return queueMailUserService.getRealUserId(searchBy)
		}
		return null
	}

	/**
	 * Search map used by search page
	 * @return
	 */
	Map getSearch() {
		def search=[
			searchBy:searchBy,
			searchFor:searchFor,
			userSearchId:userSearchId,
			status:status,
			sort:sort,
			offset:offset,
			position:position,
			order:order,
			hideUsers:hideUsers,
			jobControl:jobControl,
			max:max
		]
		return search
	}

	/**
	 * form order
	 * @param o
	 */
	void setOrder(String o) {
		order = o in ['asc', 'desc'] ? o :''
	}

	/**
	 * form max
	 * @param o
	 */
	void setMax(Integer o) {
		max= Math.min(o ? o : 10, 50)
	}

}