# Task_Manager (android application)
# Developer: 
	Kamrul Hasan
	mail: rokon.mdb@gmail.com
 	phone: 01521457927
# About:
	“Task Manager” is an Android Application where users can save tasks with description and get an alarm with the task details.


# Features Details: 
  All features details of this app, ‘Task Manager’ is describing bellow,


# Task

  # Add Task
    By clicking the add button navigate to Add Task Fragment. Users have to fill the task details as title, description, date, and time. Users can not set a task for a past date or time schedule. Task saves in Room Database.

  # Show Task List
    Home page will show all tasks from the database. Every task item shows title, description, day of week, day of month, month of year, time, menu, and status.

  # Update Task
    From the task list by clicking the menu , users can update task details as title, description, date, and time.

  # Complete Task
    From the task item menu ,users can update task status completed.

  # Delete Task
    From the task item menu the user can delete tasks.

# Alarm

  # Add Alarm
    After adding a task, this system will set an alarm with the alarm manager. Every alarm intent has a different id which is equal with task id.

  # Alarm Ringing
    There is a broadcast receiver which receives alarm action, then starts AlarmActivity. This activity will play an alarm ring. There is a close button, by clicking this button finish this activity. There will also show the event details.

  # Update Alarm
    When a user updates any task’s date time, this system will delete the previous alarm by its id, and set a new one.

  # Delete Alarm
    When a task is deleted, or completed, this system will delete the alarm for that task. 

# Calendar

  # Show Calendar
    In the home page there is a calendar icon, by clicking this popup a dialog with calendar view.

  # Show Events in Calendar
    All events will be shown in the calendar view with a particular date.

  # Show Events Name by Daily Basis
    By clicking a calendar date there will be shown all events on that day.

  # Clicking Event Title
    By clicking an event there will be shown task details dialog with title, description, day of week, day of month, month of year, time, menu, and status. Here also users can delete or update events.


# Using Tools:
 
	All using tools and third party api’s are described bellow,

  # Room Database
    For save task data using Room Database

  # Alarm Manager
	  For set alarm using Alarm manager Service

  # Broadcast Receiver
	  For receive alarm intent using Broadcast Receiver

  # MVVM
	  For data processing using MVVM design pattern.

  # Adapter
	  For task data show, using adapter pattern.

  # Navigation Graph
    For fragment navigation using nav graph 

  # Compact Calendar View
	  Using that api for custom calendar view.
    com.github.sundeepk:compact-calendar-view:3.0.0

  # Date Time Converter
    com.jakewharton.threetenabp:threetenabp:1.2.4
		This api is used for date converter in low level android versions.


# Challenges:

	Managing all date and time formatting was a little change for me. Also with the calendar view with  event show.







                                 ………………………………..
