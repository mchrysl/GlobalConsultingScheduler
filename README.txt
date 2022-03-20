
GLOBAL CONSULTING SCHEDULER
(Practical Assessment for C195-Software 2 at Western Governer's University)
This is a GUI-based desktop application that provides for the scheduling of customer appointments
for the Global Consulting Corporation with locations in Phoenix, Arizona; White Plains, New York;
Montreal, Canada; and London, England. (No relationship to any company which may or may not
actually exist.) 

Author: Margaret Chrysler (ID#: 001224428)
Email: mchrysl@wgu.edu
Version: 1.6
Date: 2/22/2021

DEVELOPMENT ENVIRONMENT
(on Windows 10 computers)
IDE Used: IntelliJ IDEA 2020.3.1 Community Version
JDK version: Java SE Version "11.0.9"
JavaFX version: javafx-sdk-11.0.2

HOW TO RUN THE PROGRAM

Login Screen
Upon starting the program the login screen should load. Where the user enteres their username
and password in the provided boxes and clicks the Login button to submit the information to
be verified.

Main Menu Screen
Once the correct username and password combination is entered,the Main Menu will load. There is
also a dialog box that pops up with a message about whether there is or is not an appointments
coming up within 15 minutes of loggin on. Click 'OK' to close the message dialog.
The Main Menu is divided into three sections. 

	The first section for customer information allows the user to choose the activity they 
	wish to perform with the customers' information: Add, Update, or Delete.
	The user needs to click on the radio button (the clear dot) next to the desired activity and 
	click the 'Change Customer Information' button which will load the Customer Information screen. 
	
	The second section for appointments allows the user to choose the activity they wish to perform 
	with the appointments: Add, Update, or Delete.
	The user needs to click on the radio button (the clear dot) next to the desired activity and
	click the 'Change Appointment Information' button which will load the Appointments screen.

	The third section provides access to three reports: one showing all appointment types per month,
	one displaying each of the contact's schedules, and one displaying the percentage of customers
	from each country.*** To display a report, simply click on the button for that report.
		
Customer Information Screen
The customer information screen displays in a table all of the customers and their associated
personal information. How the screen acts depends on the choice of activity selected in the
Main Menu which is displayed in the screen's submenu. 

	-- Add allows the user to add a new customer, but does not allow for any interaction with the 
	existing table's information. Once the information is typed in the text boxes or chosen from
	the comboBoxes, click the 'Add Customer' button to actually add the customer to the database.
	
	--Update allows the user to select a customer from the table, which then displays in the rest 
	of the screen and can then be updated. Once all the customer's information is updated, click
	the 'Update Customer' button to complete the update of the customer's information.
	
	--Delete allows the user to select a customer from the table, but not update any displayed 
	information. Upon pressing the 'Delete Customer' button, the appointments screen may open
	if the customer has any scheduled appointments that need to be deleted before the customer's 
	information can be removed. If there are no appointments to be removed, pressing the 'Delete
	Customer' button will delete the customer from the database.
	
At any time before finalizing the action (Add, Update, or Delete), it can be cancelled/aborted by
clicking the 'Close Form' button in the lower-right corner of the screen. This will close the
Customer Information screen, revealing the Main Menu again.

Appointments Screen
The appointments screen displays in a table all of the customers and their associated
personal information. How the screen acts depends on the choice of activity selected in the
Main Menu which is displayed in the screen's submenu. 

	-- Add allows the user to add a new appointment, but does not allow for any interaction wite 
	the existing table's information. Once the information is typed in the text boxes or chosen 
	from the comboBoxes, click the 'Add Appointment' button to actually add the appointment to 
	the database.
	
	--Update allows the user to select an appointment from the table, which then displays in the 
	rest of the screen and can then be updated. Once all the appointment's information is updated,
	click the 'Update Appointment' button to complete the update of the appointment's information.
	
	--Delete allows the user to select an appointment from the table, but not update any displayed 
	information. Oressing the 'Delete' button will delete the appointment from the database.
	
At any time before finalizing the action (Add, Update, or Delete), it can be cancelled/aborted by
clicking the 'Close Form' button in the lower-right corner of the screen. This will close the
Appointments screen, revealing the Main Menu again.

Appointments by Type and Month Report
This report displays a count of all appointments by Type and Month.
The report can be closed by clicking the 'Close Report' button in the lower right corner
of the screen.

Contacts' Schedules Report
This report displays a table of all of the contacts' scheduled appointments.
The report can be closed by clicking the 'Close Report' button in the lower right corner
of the screen.

Customers Per Country Report***
(the report of my choice/design)
This report displays the total  number and percentage of customers from each of the company's
countries customers, including a pie graph for clearer representation than just the numbers.
The report can be closed by clicking the 'Close Report' button in the lower right corner
of the screen.
