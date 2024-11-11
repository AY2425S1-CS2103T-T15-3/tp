---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# WardWatch Developer Guide

<!-- * Table of Contents -->
# Table of Contents
1. [Acknowledgements](#acknowledgements)
2. [Setting Up, Getting Started](#setting-up-getting-started)
3. [Design](#design)
   1. [Architecture](#architecture)
   2. [UI Component](#ui-component)
   3. [Logic Component](#logic-component)
   4. [Model Component](#model-component)
   5. [Storage Component](#storage-component)
   6. [Common Classes](#common-classes)
4. [Implementation](#implementation)
   1. [(Proposed) Undo/redo feature](#proposed-undoredo-feature)
   2. [(Proposed) Data archiving](#proposed-data-archiving)
5. [Planned Enhancements](#planned-enhancements)
6. [Documentation, Logging, Testing, Configuration, Dev-ops](#documentation-logging-testing-configuration-dev-ops)
7. [Appendix](#appendix)
   1. [Appendix: Requirements](#appendix-requirements)
   2. [Appendix: Instructions for Manual Testing](#appendix-instructions-for-manual-testing)
   3. [Appendix: Efforts](#appendix-efforts)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org). If you would like to contribute code to this project, see [se-education.org](https://se-education.org/#contributing-to-se-edu) for more info.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](https://ay2425s1-cs2103t-t15-3.github.io/tp/SettingUp.html).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command. 
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`. 
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve. 
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here is another example of interactions within the `Logic` component, taking `execute("delappt 1")` API call as an example.

<puml src="diagrams/DeleteAppointmentSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

Using this 

1. When `Logic` is called upon to execute the `delappt 1` command, it is passed to an `AddressBookParser` object which in turn creates a `DeleteCommandParser` and uses it to parse the command. 
2. This results in a `DeleteAppointmentCommand` object which is executed by the `LogicManager`. 
3. The command can communicate with the `Model` when it is executed (e.g. to delete aa appointment from a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve. 
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S1-CS2103T-T15-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Patient Feature

#### Overview
The `add` command allows users to add new patients. The command requires:
- **Name** – Patient's name.
- **ID** – Patient's unique ID.
- **Ward** – Patient's Ward.
- **Diagnosis** – Patient's Diagnosis.
- **Medication** - Patient's Medication.

Here is an activity diagram that summarises the key steps taken.
<puml src="diagrams/AddActivityDiagram.puml" alt="AddActivityDiagram" />

#### 1. Parsing User Input
The **`AddCommandParser`** class is responsible for parsing user input. It uses `ArgumentTokenizer` to tokenize the input string, extracting:
- **Name** – Patient's name.
- **ID** – Patient's unique ID.
- **Ward** – Patient's Ward.
- **Diagnosis** – Patient's Diagnosis.
- **Medication** - Patient's Medication.

The parser will check that the compulsory `Name`, `ID`, and `Ward` fields are present, and that there are no duplicate parameters included in the input string. The optional `Diagnosis` and `Medication` fields will be parsed as empty strings if they are not included in the input string.

During this parsing process:
- A `Person` instance is created to hold the relevant fields.

#### 2. Executing the Command
The **`AddCommand`** class performs the following when adding a patient:

1. **Update Existing Patient Record**:  
   The new `Person` instance will be added to the existing patient record in the **Model**.

#### 3. Exceptions and warnings

The **`AddCommand`** class enforces validation rules to ensure non-duplicates and potential incorrect input fields.

- **Ensuring non-duplicates**:
  Check that the new person added does not already exist in WardWatch, i.e. does not already exist a person with same `ID`.
<br><br>
- **Warnings**
  The command will also check for the presence of *special characters* in the `Ward` and `ID` fields. An appropriate warning will be displayed if they are present.

### Add Notes Feature

#### Overview
The `addnotes` command allows users to add notes to a specific patient. The command requires:
- **Index** – Patient's index in the address book.
- **Notes** - Patient notes that the user wishes to write for the specified patient.

Here is a sequence diagram which showcases the flow of the program as well as the key steps taken.
<puml src="diagrams/AddNotesSequenceDiagram.puml" alt="AddNotesSequenceDiagram" />

#### 1. Parsing User Input
The **`AddNotesCommandParser`** class is responsible for parsing user input. It uses `ArgumentTokenizer` to tokenize the input string, extracting:
- **Index** – Identifies the patient in the address book.
- **Notes** - Patient notes that the user wishes to write for the specified patient.

During this parsing process:
- A `Notes` instance is created to hold the note details.

#### 2. Executing the Command
The **`AddNotesCommand`** class performs the following steps to add Notes to a patient:

1. **Retrieve Patient Information**:  
   Uses the `index` from the parser to locate the patient in the latest filtered list of patients.

2. **Create New Person Instance with the Notes instance**:
    - Combines patient information with the new `Notes` details.
    - Creates a new updated `Person` instance, with the `Notes`.

3. **Replace Existing Patient Record**:
   The new `Person` instance, with the `Notes`, replaces the existing patient record in the **Model**.

4. **Updating filtered list**:
   The **Model** will then update the filtered list of patients to show all patients.

#### 3. Handling Invalid Date Inputs
The **`AddNotesCommandParser`** and **`AddNotesCommand`** classes enforce validation rules to ensure that valid index and notes are being passed in.

- **Valid index verification**:
  - **AddNotesCommandParser** checks if the `Index` is an unsigned non-zero integer.
  - **AddNotesCommand** checks if the `Index` is greater than the number of patients in the last displayed list.
  <br><br>
- **Valid notes verification**:
  - **AddNotesCommandParser** checks if the user input for `Notes` is empty and throws an error message stating that the user is unable to add empty notes to a patient.

### Add Appointment Feature

#### Overview
The `makeappt` command allows users to add an appointment tied to a specific patient. The command requires:
- **Index** – Patient's index in the address book.
- **Start Date** – Beginning date and time of the appointment.
- **End Date** – Ending date and time of the appointment.
- **Description** – Brief description of the appointment.

<puml src="diagrams/AddAppointmentSequenceDiagram.puml" alt="AddAppointmentSequenceDiagram" />

#### 1. Parsing User Input
The **`AddAppointmentCommandParser`** class is responsible for parsing user input. It uses `ArgumentTokenizer` to tokenize the input string, extracting:
- **Index** – Identifies the patient in the address book.
- **Start Date** – Beginning of the appointment.
- **End Date** – End of the appointment.
- **Description** – Additional details about the appointment.

During this parsing process:
- An `Appointment` instance is created to hold the appointment details.

#### 2. Executing the Command
The **`AddAppointmentCommand`** class performs the following steps to add an appointment:

1. **Retrieve Patient Information**:  
   Uses the `index` from the parser to locate the patient in the latest filtered list of patients.

2. **Create New Person Instance with Appointment**:
    - Combines patient information with the new `Appointment` details.
    - Creates an updated `Person` instance, including the appointment data.

3. **Replace Existing Patient Record**:
   The new `Person` instance, containing the appointment, replaces the existing patient record in the **Model**.

#### 3. Handling Invalid Date Inputs
The **`AddAppointmentCommandParser`** and **`AddAppointmentCommand`** classes enforce validation rules to ensure correct date formats and scheduling logic:

- **Format Verification**:
    - **Parser** checks if the date format follows `dd-MM-yyyy-HH-mm`.
    - **Parser** also ensures the **Start Date** is before or equal to the **End Date**.
    <br><br>
- **Conflict Checking**:
    - **Command** checks if the new appointment overlaps with any existing appointments for the patient.
    - If there is an overlap, an error message is thrown, preventing the appointment from being created.
    - If no overlap exists, the new appointment overrides any previous appointment.

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------
## **Planned Enhancements**

### `Last edited` functionality
Currently, there is no way to tell when a patient was last edited. This information might be crucial in a healthcare setting where the healthcare professional may need to know how recent the information is.<br><br>
**Planned implementation**<br><br>
We can make it such that a `Person` object contains a `LocalDateTime` field called `lastEdited` that keeps track of when the `Person` was last updated. This field will be updated whenever a new `Person` is created, be it from the `add` command or any of the other commands that edits a `Person`.
<br><br>

### Make "Index does not exist" error message more specific
Currently, whenever a user enters a command that requires `INDEX` as a parameter, such as the `view` or `delete` command, and enters an index that is either non-positive or greater than `Integer.MAX_VALUE` of `2147483647`, WardWatch will show the general `invalid command format` message followed by the command usage message. <br><br>
**Planned implementation**<br><br>
We plan to make the current invalid index message more specific and add more error messages in the case where users pass in the following invalid indexes:
- The current invalid index message which is thrown when a valid index that is greater than the largest index in the displayed patient list just states that `The person index provided is invalid` and is not specific enough. We plan to change it to `The index provided does not refer to any patient in the displayed list, please check the displayed list again!` which will give users a better idea of what went wrong.
- In the case where the index passed in is greater than the `Integer.MAX_VALUE`, the error message `The index provided is greater than 2147483647 which is unfortunately not supported in our product, please try again with a smaller index!` will be shown.
- In the case where the index passed in is non-positive, the error message `INDEX provided must be a positive integer, please try again!` will be shown.
<br><br>

### Add appointment title and description and `viewappt` command
Currently, our `makeappt` command only allows users to create appointments with a short description. This prevents users from adding too much information to the current description which may be restrictive.<br><br>
**Planned implementation**<br><br>
We plan to separate appointment title and description which gives users more flexibility. Appointment title will just be a short description such as `Surgery` or `Medical checkup` and support a character limit of 40. Appointment description will be for additional information about the appointment that the user may want to add. The appointment description will support a much longer character limit of 300 but will not be shown in the appointment list panel as the long inputs may cause issues with the UI. <br>
As such we will also implement a `viewappt` command that allows users to view all information about the appointment including the new appointment description.
<br><br>

### Add command shortcut for longer commands
Currently, some of the commands in WardWatch such as `scheduleall` and `scheduledate` are very long and may be hard to type for users. <br><br>
**Planned implementation**<br><br>
We plan to add command shortcuts for longer commands such as `sAll` and `sDate` for the commands `scheduleall` and `scheduledate` respectively. These command shortcuts will work alongside the original commands, meaning that whether the user types in `sAll` or `scheduleall`, wardwatch will recognise both as the `scheduleall` command. <br>
This is so that seasoned and more advanced users have the option to optimise their workflow by utilising the command shortcuts while newer users still have the option of using the more intuitive sounding commands which reduces the learning curve.
<br><br>

### Fix `addnotes` command showing all patients
Currently, the successful execution of an `addnotes` command will subsequently reset the last filtered list of patients to show all patients again. <br><br>
**Planned implementation**<br><br>
We plan to change the implementation of `addnotes` command such that the patient list will remain filtered after the successful addition of notes.
<br><br>

### Make duplicate ID checking case-insensitive
Currently, the checking for duplicate patient ID when creating a new patient is case-sensitive, meaning that patients with similar IDs except for the casing of letters will be considered different. For example, the IDs `P12345` and `p12345` will be considered different.<br><br>
**Planned implementation**<br><br>
We plan to make the duplicate ID cross-checking case-insensitive such that similar IDs that differ only by casing will be considered as duplicate. This will help to reduce the room for human error when using our product.
<br><br>


--------------------------------------------------------------------------------------------------------------------
## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](https://ay2425s1-cs2103t-t15-3.github.io/tp/Documentation.html)
* [Testing guide](https://ay2425s1-cs2103t-t15-3.github.io/tp/Testing.html)
* [Logging guide](https://ay2425s1-cs2103t-t15-3.github.io/tp/Logging.html)
* [Configuration guide](https://ay2425s1-cs2103t-t15-3.github.io/tp/Configuration.html)
* [DevOps guide](https://ay2425s1-cs2103t-t15-3.github.io/tp/DevOps.html)

--------------------------------------------------------------------------------------------------------------------
## **Appendix**

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* healthcare professionals
* has a need to manage a significant number of patients
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: 

* manage patients faster than a typical mouse/GUI driven app
* reduce time spent on administrative tasks by centralizing information, allowing user to focus more on patient care

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                     | I want to …​                                                                                     | So that I can…​                                                                  |
|----------|-----------------------------|--------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| `* * *`  | new user                    | see usage instructions                                                                           | refer to instructions when I forget how to use the App                           |
| `* * *`  | doctor                      | add a new patient                                                                                |                                                                                  |
| `* * *`  | doctor                      | delete a patient                                                                                 | remove entries that I no longer need                                             |
| `* * *`  | doctor                      | search for patients by name or ID                                                                | quickly find and review specific patient information                             |
| `* *`    | doctor                      | hide private contact details                                                                     | minimize chance of someone else seeing them by accident                          |
| `*`      | tech-savvy doctor           | have advanced search and filter options to quickly find and organize patient information         | easily manage large volumes of data                                              |
| `***`    | nurse                       | view a patient's medication and treatment schedule, ward location and diagnosis all in one place | I can ensure medications are administered on time and in the correct dosage      |
| `**`     | nurse                       | access a list of patients I am responsible for during my shift                                   | I can manage my time efficiently and ensure that all patients receive timely care |
| `* * *`  | As a detail-oriented doctor | add notes to patients                                                                            | manage information about the patient                
| `* * *`  | doctor                      | edit my patients' information                                                                    | I can update their conditions as they change                             
| `* * *`  | forgetful doctor            | receive daily reminders on the current day's appointment                                         | I don’t overlook any important tasks or visit schedules.                             

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `WardWatch` and the **Actor** is the `doctor`, unless specified otherwise)

**Use case: UC01 - Add a patient**

**MSS**

1. Doctor submits new patient information
2. WardWatch displays a success message containing information of new patient

   Use case ends.

**Extensions**

* 1a. The information entered is invalid

    * 1a1. WardWatch shows an invalid patient information error message.

      Use case resumes at step 1.

* 1b. The format of the input is invalid
    * 1b1. WardWatch shows an invalid format error message.
        
      Use case resumes at step 1.

**Use case: UC02 - Delete a patient**

**Preconditions**: WardWatch is displaying a non-empty list of patients
**MSS**
1. Doctor requests to delete a specific patient from the displayed list
2. WardWatch deletes the patient

    Use case ends.

**Extensions**

* 1a. Doctor request to delete invalid patient

    * 1a1. WardWatch shows an invalid patient message.

      Use case resumes at step 1.

**Use case: UC03 - Update a patient**

**Preconditions**: WardWatch is displaying a non-empty list of patients
**MSS**

1. Doctor submits new patient information of specific patient
2. WardWatch displays information of updated patient

   Use case ends.

**Extensions**

* 1a. The information entered is invalid

    * 1a1. WardWatch shows an invalid patient information error message.

      Use case resumes at step 1.

* 1b. The format of the input is invalid
    * 1b1. WardWatch shows an invalid format error message.

      Use case resumes at step 1.

**Use case: UC04 - Search a patient**

**MSS**

1. Doctor searches for patients
2. WardWatch shows a list of patients matching the search

    Use case ends.

**Extensions**

* 2a. Doctor tries to do an invalid search
    * 2a1. WardWatch shows an invalid search error message.
        
        Use case resumes at step 1.

* 2b. There is no patient that matches the search

    * 2b1. WardWatch shows that there are no matching patients.

        Use case ends.

* 2c. The format of the input is invalid
    * 2c1. WardWatch shows an invalid format error message.

      Use case resumes at step 1.

**Use case: UC05 - View patient**

**Preconditions**: WardWatch is displaying a non-empty list of patients
**MSS**

1. Doctor request to view a specific patient from the list
2. WardWatch displays information about the specific patient

   Use case ends.

**Extensions**

* 1a. Doctor request to view invalid patient

    * 1a1. WardWatch shows an invalid patient message.

      Use case resumes at step 1.

**Use case: UC06 - List patients**

**MSS**

1. Doctor request to list all patients
2. WardWatch shows a list of all patients

   Use case ends.

**Extensions**

* 2a. The list is empty

    * 2a1. WardWatch shows that list is empty

      Use case ends.

**Use case: UC07 - Add Appointment**

**MSS**

1. Doctor submits new Appointment information for a patient
2. WardWatch displays success message with the updated patient information
    
    Use case ends.

**Extensions**

* 1a. The information entered is invalid

    * 1a1. WardWatch shows an invalid Appointment information error message.

      Use case resumes at step 1.

* 1b. The format of the input is invalid
    * 1b1. WardWatch shows an invalid format error message.

      Use case resumes at step 1.

**Use case: UC08 - Delete Appointment**

**Preconditions**: WardWatch is displaying a non-empty list of patients
**MSS**

1. Doctor request to delete an Appointment tied to a patient
2. WardWatch deletes specified appointment

   Use case ends.

**Extensions**

* 1a. The delete appointment command format entered is invalid

    * 1a1. WardWatch shows an incorrect format error message.

      Use case resumes at step 1.

* 1b. Doctor requests to delete an Appointment from an invalid patient

    * 1b1. WardWatch shows an invalid patient message.
  
* 1c. Doctor requests to delete a non-existing Appointment from a patient
*
    * 1c1. WardWatch shows patient does not have Appointment error message.

      Use case resumes at step 1.

**Use case: UC09 - Change Appointment**

**MSS**

1. Doctor <ins> deletes existing Appointment(UC08) </ins>
2. Doctor <ins> adds new Appointment with updated details(UC07) </ins>

   Use case ends.

**Use case: UC10 - See Schedule for a certain day**

**MSS**

1. Doctor request to see schedule for a certain day
2. WardWatch displays all appointments for that day

   Use case ends.

**Extensions**

* 1a. The date format is invalid

    * 1a1. WardWatch shows an invalid date error message.

      Use case resumes at step 1.

**Use case: UC11 - See Schedule for all appointments**

**MSS**

1. Doctor request to see schedule for all days
2. WardWatch displays all appointments for all days

   Use case ends.

**Extensions**

* 1a. The schedule all command format is invalid

    * 1a1. WardWatch shows an error message.

      Use case resumes at step 1.

**Use case: UC12 - Add notes tied to a specific patient**

**MSS**

1. Doctor submits new notes for a certain patient
2. WardWatch displays patient information with notes 

   Use case ends.

**Extensions**

* 1a. Patient Notes format is invalid 

    * 1a1. WardWatch shows an invalid note description error message.

      Use case resumes at step 1.

**Use case: UC13 - delete notes to a specific patient**

**MSS**

1. Doctor request to delete notes for a certain patient
2. WardWatch deletes the patient notes

   Use case ends.

**Extensions**

* 1a. Patient Notes field is empty

    * 1a1. WardWatch shows a notes is already empty error message.

      Use case resumes at step 1.



### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The system should respond to user actions within 1 second under normal load.
5. Will not have a server or cloud storage system. All data will be stored in local storage.
6. The system should handle errors gracefully, providing meaningful messages to users without crashing.
7. The system should be able to work offline, in the absence of internet connection.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Patient**: A person that has been designated to be under the care of the user(i.e. a doctor or nurse).
* **Local Storage**: A text file, with read and write properties, located relative to the application file.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Adding a patient

**Command:** `add`<br>

1. Adding a patient with all fields
    * **Prerequisites:**
        * No patients in the list
         <br><br>
   * **Test Case:** `add n/John Doe i/P00001 w/A1 d/Type 1 Diabetes m/Metformin`<br>
   * **Expected** A patient with the following fields is added to the list:
       * Name: `John Doe`
       * ID: `P00001`
       * Ward: `A1`
       * Diagnosis: `Type 1 Diabetes`
       * Medication: `Metformin`
       * Notes: `-`
       * Appointment: `-`
         <br><br>
2. Adding a patient without optional fields (DIAGNOSIS and MEDICATION)
    * **Prerequisites:**
        * No patients in the list
        <br><br>
    * **Test Case:** `add n/Kathy Prince i/P00002/D1 d/Gastrisitis`<br>
    * **Expected** A patient with the following fields is added to the list:
        * Name: `John Doe`
        * ID: `P00001`
        * Ward: `A1`
        * Diagnosis: `Type 1 Diabetes`
        * Medication: `-`
        * Notes: `-`
        * Appointment: `-`
    <br><br>
   * **Test Case:** `add n/Joshua Lim i/P00003 w/C3 m/Paracetemol`<br>
   * **Expected** A patient with the following fields is added to the list:
        * Name: `Joshua Lim`
        * ID: `P00003`
        * Ward: `C3`
        * Diagnosis: `-`
        * Medication: `Paracetemol`
        * Notes: `-`
        * Appointment: `-`
          <br><br>
3. Adding a patient without optional fields (DIAGNOSIS and MEDICATION)
    * **Prerequisites:**
        * No patients in the list
      <br><br>
   * **Test Case:** `add n/Emily Tan i/P00004 w/B2`
   * **Expected:** A patient with the following fields is added to the list:
       * Name: `Emily Tan`
       * ID: `P00004`
       * Ward: `B2`
       * Diagnosis: `-`
       * Medication: `-`
       * Notes: `-`
       * Appointment: `-`
         <br><br>
### Editing a patient

**Command:** `edit`<br>

1. Editing a patient with all fields
    * **Prerequisites:**
        * Non-empty patient list
        * First patient in the list does have fields that match the edited fields
          <br><br>
    * **Test Case:** `edit 1 n/Jeff Bean i/P10000 w/G5 d/influenza m/paracetomol`
    * **Expected:** The first patient in the list is updated with the following fields:
      * Name: `Jeff Bean`
      * ID: `P10000`
      * Ward: `G5`
      * Diagnosis: `influenza`
      * Medication: `paracetomol`
      * Notes: remains unchanged
      * Appointment: remains unchanged
        <br><br>
2. Editing a patient with a few fields
    * **Prerequisites:**
        * Non-empty patient list
        * First patient in the list does have fields that match the edited fields
          <br><br>
    * **Test Case 1:** `edit 1 n/Samuel Lee`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: `Samuel Lee`
        * ID: remains unchanged
        * Ward: remains unchanged
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: remains unchanged
        * Appointment: remains unchanged
          <br><br>
    * **Test Case 2:** `edit 1 i/P20001 w/C1`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: `P20001`
        * Ward: `C1`
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: remains unchanged
        * Appointment: remains unchanged
          <br><br>
    * **Test Case 3:** `edit 1 d/Bronchitis m/Amoxicillin w/D2`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: remains unchanged
        * Ward: `D2`
        * Diagnosis: `Bronchitis`
        * Medication: `Amoxicillin`
        * Notes: remains unchanged
        * Appointment: remains unchanged
          <br><br>
### Deleting a patient

**Command:** `delete`<br>

1. Deleting a patient while all patients are being shown
    * **Prerequisites:**
        * Non-empty patient list
        * List all patients using the `list` command
          <br><br>
    * **Test Case 1:** `delete 1`
    * **Expected:** First patient is deleted from the list. Details of the deleted contact shown in the status message.
      <br><br>
    * **Test Case 2:** `delete 0`
    * **Expected:** No patient is deleted. Error details shown in the status message.
      <br><br>
    * **Test Case 3:** Other incorrect delete commands to try: `delete`, `delete x` (where x is larger than the list size)
    * **Expected:** No patient is deleted. Error details shown in the status message.
      <br><br>
### Searching for a patient

**Command:** `find`<br>

1. Finding patients by their information.
   * **Prerequisites:**
        * Non-empty patient list
         <br><br>
   * **Test Case 1:** `find n/Emily Tan`
   * **Expected:** Displays all patients with **Name** `Emily Tan`.
     <br><br>
   * **Test Case 2:** `find i/P00001`
   * **Expected:** Displays all patients with **ID** `P00001`.
     <br><br>
   * **Test Case 3:** `find w/A1`
   * **Expected:** Displays all patients with **Ward** `A1`.
     <br><br>
   * **Test Case 4:** `find d/Diabetes`
   * **Expected:** Displays all patients with **Diagnosis** `Diabetes`.
     <br><br>
   * **Test Case 5:** `find m/Metformin`
   * **Expected:** Displays all patients with **Medication** `Metformin`.
     <br><br>
### Viewing a patient

**Command:** `view`<br>

1. Viewing a patient's information.
    * **Prerequisites:**
        * Non-empty patient list
          <br><br>
    * **Test Case 1:** `view 1`
    * **Expected:** Displays the first patients information in the command result box.
      <br><br>
### Adding notes to a patient

**Command:** `addnotes`<br>

1. Adding notes to a patient
    * **Prerequisites:**
        * Non-empty patient list
          <br><br>
    * **Test Case:** `addnotes 1 pn/patient prone to falling `
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: remains unchanged
        * Ward: remains unchanged
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: `patient prone to falling`
        * Appointment: remains unchanged
          <br><br>
2. Adding empty patient notes
    * **Prerequisites:**
        * Non-empty patient list
          <br><br>
    * **Test Case:** `addnotes 1 pn/`
    * **Expected:** WardWatch throws an error informing the user that they are unable to add an empty note to a patient.
      <br><br>
### Deleting notes from a patient

**Command:** `delnotes`<br>

1. Deleting notes from a patient that has notes
    * **Prerequisites:**
        * Non-empty patient list
        * First patient in the list must have notes
          <br><br>
    * **Test Case:** `delnotes 1`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: remains unchanged
        * Ward: remains unchanged
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: `-`
        * Appointment: remains unchanged
          <br><br>
2. Deleting notes from a patient who does not have notes
    * **Prerequisites:**
        * Non-empty patient list
        * First patient in the list must not have any notes
          <br><br>
    * **Test Case:** `delnotes 1`
    * **Expected:** WardWatch throws an error informing the user that the patient does not have any notes to delete.
      <br><br>
### Making an appointment

**Command:** `makeappt`<br>

1. Adding an appointment to a patient
    * **Prerequisites:**
        * Non-empty patient list
        * The appointment the user adds must not overlap with any existing appointment
          <br><br>
    * **Test Case:** `makeappt 1 a/ Surgery s/ 01-01-2024-20-00 e/ 01-01-2024-23-00`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: remains unchanged
        * Ward: remains unchanged
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: remains unchanged
        * Appointment: `Surgery FROM 01 January 2024, 08:00 pm TO 01 January 2024, 11:00 pm`
          <br><br>
2. Adding an appointment that overlaps with other appointments
    * **Prerequisites:**
        * Non-empty patient list
        * The appointment the user adds must overlap with an existing appointment
          <br><br>
    * **Test Case:** `makeappt 1 a/ Checkup s/ 02-01-2024-20-00 e/ 02-01-2024-23-00`
    * **Expected:** Ward watch throws the error message `Appointment overlaps with another pre-existing appointment! Please check your schedule and try again`
          <br><br>
### Deleting an appointment

**Command:** `delappt`<br>

1. Deleting an appointment from a patient who has an appointment
    * **Prerequisites:**
        * Non-empty patient list
        * First patient has an appointment
          <br><br>
    * **Test Case:** `delappt 1`
    * **Expected:** The first patient in the list is updated with the following fields:
        * Name: remains unchanged
        * ID: remains unchanged
        * Ward: remains unchanged
        * Diagnosis: remains unchanged
        * Medication: remains unchanged
        * Notes: remains unchanged
        * Appointment: `-`
          <br><br>
2. Deleting an appointment from a patient who has no appointment
    * **Prerequisites:**
        * Non-empty patient list
        * First patient in the list has no appointment
          <br><br>
    * **Test Case:** `delappt 1`
    * **Expected:** Ward watch throws the error message `The Patient indicated does not have an appointment`
      <br><br>
### Showing appointments on a particular date

**Command:** `scheduledate`<br>

1. Show schedule on a particular date which has appointments
    * **Prerequisites:**
        * Non-empty patient list
        * At least one patient has an appointment
        * The date that you pass into the command as input has at least one appointment
          <br><br>
    * **Test Case:** `scheduledate 01-01-2024`
    * **Expected:** WardWatch will give a message stating the number of appointments listed on that date and the appointment list will show all of those appointments.
      <br><br>
2. Show schedule on a particular date which has no appointments
    * **Prerequisites:**
        * Non-empty patient list
        * The date that you pass into the command as input has 0 appointments
          <br><br>
    * **Test Case:** `scheduledate 02-01-2024`
    * **Expected:** WardWatch shows the message `0 appointments on 02 January 2024 listed` and the appointments list shows no appointments.
      <br><br>
### Showing all appointments

**Command:** `scheduleall`<br>

1. Show all appointments
    * **Prerequisites:**
        * Non-empty patient list
        * At least one patient has an appointment
          <br><br>
    * **Test Case:** `scheduleall`
    * **Expected:** WardWatch shows the message `Displayed all appointments` and the appointment list will show all appointments.
      <br><br>

## **Appendix: Efforts**

This appendix provides an overview of the effort and complexity involved in modifying the original AB3 (Address Book Level 3) to create WardWatch. WardWatch extends AB3’s basic structure to support a healthcare-focused system, which manages multiple entity types and introduces new functionalities like handling patient information, medications, and appointments.

### Logic (Parser)
1. The Parser component required significant modifications to accommodate the new entity types and fields introduced in WardWatch. Challenges included:

    * **Tokenization**: Setting specific conditions for the tokenizer to handle new fields, such as medication and appointment, was complex. This required carefully configuring the tokenizer to recognize and differentiate between new input formats and ensure accurate data processing.
    * **Input Validation**: To handle healthcare-related data, validation had to be strict to avoid incorrect entries. This involved refining the Parser logic to enforce stricter data checks than AB3, requiring additional time to test and ensure robust error handling.

2. The FindCommandParser was challenging to implement as we wanted the command to be able to search different fields of patients, which meant that there were many different scenarios we had to account for. This increase in complexity made it harder for us to ensure that error inputs would fail graciously, as there were now more cases to consider.

### Logic (Command)
1. Creating new commands, especially the AddAppointment and AddMedication commands, presented unique challenges:
    * **Command Structure Adaptation**: Extending the existing command structure for WardWatch was necessary to support new types of commands without impacting AB3’s base functionality. This required carefully designing new command classes to integrate seamlessly with existing entities.
    * **Difficulty in Implementing AddAppointment**: The AddAppointment command was particularly challenging because it required understanding how to schedule and validate appointments within the existing architecture. This command also had to be compatible with patient data, ensuring appointments were correctly linked.

### Model
1. The Model component needed restructuring to support multiple entity types, such as patients, medications, and appointments, which added complexity:
   * **Data Relationships**: Establishing relationships between entities, such as linking patients with medications and appointments, was challenging. Designing these relationships in a way that allowed for seamless data manipulation without creating dependencies was an important aspect of the model redesign.

2. We had difficulty trying to implement a `SortedList` that was meant to keep track of the schedules that are meant to be shown, and returning them in a sorted manner. Challenges included:
   * **List type**: Although it might have been more intuitive to create a `FilteredList` of `Appointment` rather than `Person`, we require certain details about the patient. Hence, we had to make the list keep track of `Person` to ensure this functionality.
   * **Sorting a filtered list**: As we wanted to have the appointments be returned in order, we had to sort the list. However, we could not find a way to do this seamlessly, other than to create a `filteredList` and sort it from there. This could potentially be because of our lack of familiarity with `javaFX`.
   
### Storage
1. With the addition of the `Appointment` class, we had to learn how the JSON format worked, and how we could utilise its functionalities to allow for the successful storing and reading of data, now that every `Person` has an `Appointment` field.
2. It was necessary to get a basic understanding of how JSON files work. Upon creating our new Json class that adapts to appointment, a weak understanding of `@JsonCreator` property caused initial bugs where appointment information had issues being retrieved from the Json file format. This bug was eventually fixed with greater understanding of Json.

### UI
1. UI Design required good understanding and identification of the relevant dependencies on the corresponding .fxml and .css files.
2. To restructure and customise the UI, a firm understanding of UI elements such as the HBox, VBox and Stackpanes is necessary to layout the UI elements according. There was particularly a struggle understanding the effects of the growing Hbox and Vbox, as the space occupied is unexpected.
3. Choice or colour to make the app aesthetic is also non-trivial as it is difficult to find matching colours that are aesthetic.
4. The setting of result boxes also required consideration, as too long or short may cause data to not be represented clearly, due to the text overflowing. It is also a tradeoff for space with the other elements.

