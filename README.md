InCycle Clinic
================

Welcome to InCycle clinic management system. This system is designed to help manage the day-to-day operations of a clinic. 
It is designed to be used by the staff.

How does it work?
-----------------

You can use it to add new patients, view existing patients, add new appointments, view existing appointments and do invoicing.


Problems and issues (from the client's perspective)
---------------------------------------------------

1. This application is installed in console mode for every customer, so updating it is a bit of a hassle.
2. The application is not user-friendly and requires a lot of training to use, although savvy users work it very fast.
3. The application users handling is problematic.
4. The application online presence is not existent.
   * Patients can't book appointments online.
   * Patients can't view their invoices online, or pay them.
   * Patients can't view their medical history online.
   * Patients can't view their upcoming appointments online.
   * Patients can't get reminders for their upcoming appointments.
5. The application is not able to handle multiple clinics on different locations.
6. No pre error handling, e.g. still asking for an invoice number when there are no invoices.
7. no easy way to tell who much a patient owes.
8. No real search
9. Nurses have to ask the operator to execute commands on the system, as they don't have access to the system.


Problems and issues (from the developer's perspective)
------------------------------------------------------

* This product is in production for a long time, and it is being used by many clinics. 
* The code is a bit of a mess, and it is hard to maintain. 
* The code is not modular, and it is hard to add new features. 
* The code is not object-oriented, and it is hard to understand.
* Error handling is lacking, the application just exits when an error occurs.
* There is no reliable way to test the application without installing a database.

Security issues
----------------

* The application is not secure, and it is vulnerable to SQL injection attacks.
* The developer create a loading from an insecure file
* output is not user-friendly. and uses IDs instead of names.
