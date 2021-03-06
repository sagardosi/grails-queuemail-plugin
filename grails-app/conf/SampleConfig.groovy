import org.grails.plugin.queuemail.enums.QueueTypes

queuemail {
	
	//standardRunnable = true
	emailPriorities = [
					defaultExample:org.grails.plugin.queuemail.enums.Priority.REALLYSLOW
	]

	// This is an override of grails { mail { configuration method allowing many mail senders
	
	// The configuration for DefaultExampleMailingService has set this to be 2 emails
	// Meaning after 2 it will fall over to 2nd Configuration

	exampleFrom="usera <userA@gmail.com>"
	exampleTo="userA_ReplyTo <userA@gmail.com>"

	mailConfigExample1 {
		host = "smtp.gmail.com"
		port = 587
		username = "userA@gmail.com"
		password = 'PASSWORD'
		props = ["mail.debug":"true",
				 "mail.smtp.user":"userA@gmail.com",
				 "mail.smtp.host": "smtp.gmail.com",
				 "mail.smtp.port": "587",
				 "mail.smtp.auth": "true",
				 "mail.smtp.starttls.enable":"true",
				 "mail.smtp.EnableSSL.enable":"true",
				 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				 "mail.smtp.socketFactory.fallback":"false",
				 "mail.smtp.socketFactory.port":"465"
		]
	}
	mailConfigExample1.fromAddress="USERAAA <userA@gmail.com>"

	mailConfigExample2 {
		host = "smtp.gmail.com"
		port =587
		username = "userB@gmail.com"
		password = 'PASSWORD'
		props = ["mail.debug":"true",
				 "mail.smtp.user":"userB@gmail.com",
				 "mail.smtp.host": "smtp.gmail.com",
				 "mail.smtp.port": "587",
				 "mail.smtp.auth": "true",
				 "mail.smtp.starttls.enable":"true",
				 "mail.smtp.EnableSSL.enable":"true",
				 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
				 "mail.smtp.socketFactory.fallback":"false",
				 "mail.smtp.socketFactory.port":"465"
		]
	}
	mailConfigExample2.fromAddress="userBB<userB@gmail.com>"
	
	/*
	 * keepAliveTime in seconds
	 */
	keepAliveTime=300
	
	/*
	 * corePoolSize this should match maximumPoolSize
	 * 
	 */
	corePoolSize=3
	/*
	 * maxPoolSize
	 *
	 */
	maximumPoolSize=3
	
	/*
	 * Amount of elements that can queue
	 */
	maxQueue=100
	
	
	/*
	 * If you have 3 threads and there are 6 reports launched
	 * 
	 * If after (reportThreads - preserveThreads) =
	 * 3 - 1 = 2
	 * 
	 * After 2 threads all report priorities above or equal
	 * perservePriority Permission  = Priority.MEDIUM
	 * will be left in queued state.
	 * This means all those below Priority.MEDIUM will have a spare slot 
	 * to run within. In short an fast lane left open always for those 
	 * below medium and 2 slow lanes. You should be able to configure 6 report 
	 * Threads and 2 reserveThreads. Configure to suite your needs  
	 */
	preserveThreads = 1
	
	// Explained in preseverThreads
	preservePriority = org.grails.plugin.queuemail.enums.Priority.MEDIUM

	defaultEmailQueue=QueueTypes.ENHANCED // org.grails.plugin.queuemail.enums.queueTypes.BASIC


	/*
	 * Enhanced Priority task killer
	 * Kill a running task that runs more than ?
	 * in seconds - which gives you a chance to
	 * give more accuracy than just minutes alone
	 * 60 = 1 minute
	 * 600 = 10 minutes
	 * 3600 = 1 hour
	 *
	 * By default this is 0 = off
	 *
	 */
	killLongRunningTasks=300

	/*
	 * defaultComparator will use out of the box 
	 * comparator method for either of PriorityBlocking
	 * or EnhancedPriorityBlocking
	 * 
	 * If enabled all of the rest of the experimental options below it won't kick in
	 * By default it is off
	 *  
	 */
	defaultComparator=false

	
	/*
	 * Configure this value if you have enabled in BootStrap:
	 *
	 * executorBaseService.rescheduleRequeue()
	 *
	 * This will then attempt to re-trigger all outstanding jobs upon an
	 * application restart.
	 *
	 * Running the above task without below enabled will simply set the
	 * status of any jobs of running back to queued. Making them ready to be
	 * processed. They were tasks that had been running whilst application was
	 * interrupted/stopped.
	 *
	 * You could also use the manual checkQueue method provided on the listing UI
	 *
	 */
	checkQueueOnStart=true
	
	
	/*
	 * DisableExamples basically disables the examples controller
	 * so when you have tested and don't wish to allow this controller to be available on your app
	 * then turn it off through this config set it to true. By default it is false
	 */
	disableExamples=false
	
	/*
	 * if you no longer wish to display queue type on listing screen set this to true
	 */
	hideQueueType=false
	
	/*
	 * If you no longer wish to show report priority on report screen switch this to true 
	 */
	hideQueuePriority=false

	/**
	 * -----------------------------------------------------
	 * Configuration for unreliable smtp services
	 *
	 * Failure attempts - re-attempts
	 * -----------------------------------------------------
	 */

	/**
	 * If 5 emails fail consecutively it will then mark the smtp service as down
	 * Global configuration for all. By default this is 5.
	 *
	 * 5 emails failing will also fall inline with - email resending if it could not send.
	 * If your service has 2 configurations and first element has 10 limit and this value is configured as 2
	 *
	 * Upon triggering your first email, that first email will attempt delivery if it fails, it will try again and it
	 * will hit this same host - the counter will increment - after 2 failures it gives up and moves to next config
	 *
	 * All new emails will now bypass that first config and go to 2nd configuration
	 *
	 */
	failuresTolerated =  5 // 5  failures in a row

	/**
	 * Once 300 new queueId's have gone through - by default
	 * change this to another value
	 * once it goes beyond this value it will add that above marked down host back into the pool and try to send email
	 * It will again go through above failuresTolerated (if still down) and fail
	 */
	elapsedQueue = 300

	/**
	 * If the system does not generate much emails then the OR if it hits 1800 seconds (30 minutes) by default
	 * like above it will smtp config back into pool
	 */
	elapsedTime =  1800 // 30 minutes


	/**
	 * smtpValidation =  pre-delivery confirmation (Experimental) true/false by default false
	 *
	 * You should only use this option if you have access to the internet on port 25.
	 *
	 * Most home broadband connections reject direct port 25 connections to the outside world
	 *
	 * If set to true you must also use either of these methods over to/cc/bcc : (refer to QueueTestController)
	 *
	 //message.cleanTo(['aa <aa@aa>','bb','cc','dd <dd@example.com>','ee <ee@example.com>'])
	 //message.cleanBcc(['aa <aa@aa>','bb','cc','dd <dd@example.com>','ee <ee@example.com>'])
	 //message.cleanCc(['aa <aa@aa>','bb','cc','dd <dd@example.com>','ee <ee@example.com>'])


	 * This will attempt to check the email address of the recipient from the first MX bound
	 * to their email address. If valid then the email address is silently added.
	 *
	 * Forward checking if recipient email will accept email from you and not far from delivery confirmation
	 *
	 */
	smtpValidation=false


}

