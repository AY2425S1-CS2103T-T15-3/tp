---
  layout: default.md
    title: "User Guide"
    pageNav: 3
---

# WardWatch User Guide

WardWatch (WW) is a **desktop app for managing patients information in hospitals, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, WW can get your patient management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
# Table of Contents
1. [Quick Start](#quick-start)
2. [Features](#features)
    - [Viewing help](#viewing-help--help)
    - [Adding a person](#adding-a-person--add)
    - [Listing all patients](#listing-all-patients--list)
    - [Editing a patient](#editing-a-patient--edit)
    - [Searching patients by field](#searching-patients-by-field--find)
    - [Deleting a person](#deleting-a-person--delete)
    - [Adding an appointment to a person](#adding-an-appointment-to-a-person--make_appt)
    - [List all person appointment](#list-all-person-appointment--schedule_all)
    - [Clearing all entries](#clearing-all-entries--clear)
    - [Exiting the program](#exiting-the-program--exit)
    - [Saving the data](#saving-the-data)
    - [Editing the data file](#editing-the-data-file)
    - [Archiving data files](#archiving-data-files-coming-in-v20)
3. [FAQ](#faq)
4. [Known Issues](#known-issues)
5. [Command Summary](#command-summary)

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar wardwatch.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all patients.

    * `add /id P23456 /name Donald Duck /ward B5 /diagnosis Diabetes /medications Insulin` : Adds a contact named `Donald Duck` to the patient list.

    * `delete 3` : Deletes the 3rd patient shown in the current list.

    * `find w/ B1` : Finds all patients with ward B1.

    * `clear` : Deletes all patients.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>

[Back to Table of Contents](#table-of-contents)

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

[Back to Table of Contents](#table-of-contents)

### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME i/ID w/WARD d/DIAGNOSIS m/MEDICATION`

<box type="tip" seamless>

</box>

Examples:
* `add n/John Doe i/P12345 w/A1 d/TYPE 1 DIABETES m/METFORMIN `
* `add n/Nicky Lam i/P17777 w/A5 d/Gastritis m/Proton pump inhibitors `

[Back to Table of Contents](#table-of-contents)

### Listing all patients : `list`

Shows a list of all patients in WardWatch.

Format: `list`

[Back to Table of Contents](#table-of-contents)

### Editing a patient : `edit`

Edits an existing person in the address book.

Format: `edit INDEX i/ID w/WARD d/DIAGNOSIS m/MEDICATION`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
*  `edit 1 i/P12345 w/A2` Edits the patient ID and ward of the 1st person to be `P12345` and `A2` respectively.
*  `edit 2 n/Betsy Crower m/Paracetamol` Edits the name and medication of the 2nd person to be `Betsy Crower` and `Panadol`

[Back to Table of Contents](#table-of-contents)

### Viewing a patient's details: `view`

Displays more details about a specific patient listed.

Format: `view INDEX`

* Shows the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Displays additional information such as a patient's `ward`, `diagnosis`, `medication` and `notes(if any)`.

Examples:
* `view 2` to view the 2nd patient's details.
![result for 'view 2'](images/viewResult.png)

### Searching patients by field: `find`

Finds patients whose specified field contain any of the given keywords.

Format: `find FIELD/ KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Able to search any field, but only one field at a time.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Valid fields for `find` Command:

* Name: Use `n/` to search by patient name.
* ID: Use `i/` to search by patient ID.
* Ward: Use `w/` to search by ward.
* Diagnosis: Use `d/` to search by diagnosis.
* Medication: Use `m/` to search by medication.

Examples:
* `find n/ John` returns `john` and `John Doe`
* `find w/ B1` returns all patients in ward B1
* `find i/ Dave` returns an empty list
* `find n/ alice benson` returns `Alice Pauline`, `Benson Meier`<br>
![result for 'find n/ alice benson'](images/findAliceBensonResult.png)

[Back to Table of Contents](#table-of-contents)

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

[Back to Table of Contents](#table-of-contents)

### Adding an Appointment to a person: `make_appt`

Makes an appointment for a person

Format: `make_appt INDEX a/APPOINTMENT_DESCRIPTION s/START_DATE_TIME e/END_DATE_TIME`

* Adds appointment to the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* The `APPOINTMENT_DESCRIPTION` refers to the type of appointment.
* `START_DATE_TIME` and `END_DATE_TIME` refers to the date and time the appointment starts and ends respectively.
* The start **must be before** the end date and time.

Examples:
* `list` followed by `make_appt 1 a/Surgery s/23-10-2024-12-00 e/23-10-2024-15-00` adds a `Surgery` appointment to the
  1st person in the address book that is on the 23rd of October 2024 from 12pm to 3pm.

[Back to Table of Contents](#table-of-contents)

### List all person appointment: `schedule_all`

Lists all the appointments assigned to every person

Format: `schedule_all`

Examples:
* `list` followed by `make_appt 1 a/Surgery s/23-10-2024-12-00 e/23-10-2024-15-00` adds a `Surgery` appointment to the
  1st person in the address book that is on the 23rd of October 2024 from 12pm to 3pm.

[Back to Table of Contents](#table-of-contents)

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

[Back to Table of Contents](#table-of-contents)

### Exiting the program : `exit`

Exits the program.

Format: `exit`

[Back to Table of Contents](#table-of-contents)

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

[Back to Table of Contents](#table-of-contents)

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

[Back to Table of Contents](#table-of-contents)

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find**   | `find FIELD/ KEYWORD [MORE_KEYWORDS]`<br> e.g., `find n/ James Jake`
**List**   | `list`
**Make_appt** | `make_appt INDEX a/APPOINTMENT_DESCRIPTION s/START_DATE_TIME e/END_DATE_TIME`<br> e.g.,`make_appt 1 a/Surgery s/23-10-2024-12-00 e/23-10-2024-15-00`
**Schedule_all**| `schedule_all`<br> e.g.,`schedule_all`
**Help**   | `help`

[Back to Table of Contents](#table-of-contents)
