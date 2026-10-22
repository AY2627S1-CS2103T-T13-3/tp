---
layout: page
title: User Guide
---

PressMemo is a **desktop application for journalists and reporters to manage contacts, stories, and follow-ups, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, PressMemo can help you organize sources across multiple stories and track follow-up reminders faster than traditional GUI applications.

This guide describes the features implemented for PressMemo v1.4.

* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the PressMemo `.jar` file from [here](https://github.com/AY2627S1-CS2103T-T13-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for PressMemo. The commands below assume the file is named `pressmemo.jar`.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar pressmemo.jar`.<br>
   A GUI with **CONTACTS**, **STORIES**, and **FOLLOW-UPS** panels should appear in a few seconds. On the first run, the app contains sample contacts, an empty story list, and no follow-ups.<br>
   The image below illustrates the application interface; some visual details may vary.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

    * `list` : Displays all contacts and stories, clearing both filters.

    * `listcontacts` : Displays all contacts without changing the story list.

    * `liststories` : Displays all stories without changing the contact list.

    * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

    * `addstory Housing Investigation` : Creates a story named `Housing Investigation`.

    * `assign ci/1 si/1` : Assigns the 1st displayed contact to the 1st displayed story.

    * `listcontacts si/1` : Displays only contacts assigned to the 1st displayed story.

    * `liststories ci/1` : Displays only stories assigned to the 1st displayed contact.

    * `addfollowup Interview prep ci/1` : Adds a follow-up reminder for the 1st displayed contact.

    * `deletefollowup 1` : Deletes the follow-up at index 1.

    * `shortcut s/ls c/list` : Defines `ls` as a shortcut for `list`.

    * `ls` : Executes `list` after the shortcut above has been assigned.

    * `unshortcut ls` : Removes the custom `ls` shortcut.

    * `delete 1` : Deletes the 1st displayed contact and removes their story assignments and follow-ups.

    * `clear` : Deletes all contacts, stories, assignments, and follow-ups.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

The **CONTACTS** panel shows each person's details, tags, and assigned story names. The **STORIES** panel shows each story's name, assigned contact names, and contact count. The **FOLLOW-UPS** panel shows reminders together with their associated contacts. A contact can belong to several stories and have multiple follow-ups; a story can have several contacts.


### Notes about the command format

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.
* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.
* Items followed by `…`​ can appear zero or more times.<br>
  For example, `[t/TAG]…​` may be omitted, or written as `t/friend` or `t/friend t/family`.
* Prefixed parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.
* Prefixes end with `/` (e.g., `n/`, `p/`, `ci/`, `si/`, `s/`, and `c/`). Unrecognized prefixes may be rejected.
* Indices refer to the numbers in the currently displayed lists, including filtered lists. `ci/CONTACT_INDEX` selects a contact, `si/STORY_INDEX` selects a story, and a bare `INDEX` selects an entry according to the command. Each index must be a positive integer within the relevant displayed list. Check the numbers after changing a list.
* Command words and prefixes are case-sensitive. Use lowercase, such as `addstory`, `addfollowup`, `shortcut`, `ci/`, `si/`, `s/`, and `c/`.
* Extraneous parameters for commands that take no parameters, such as `help`, `exit`, `clear`, and `list`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.
* The refactored listing commands are `list`, `listcontacts`, and `liststories`. Enter each as **one command word**; `list contacts`, `list stories`, `list f`, and `listfollowups` are not supported.
* `listcontacts` and `liststories` accept only their optional filters described below; unrelated arguments are rejected.
* Follow-ups are shown in the **FOLLOW-UPS** panel. There is currently no separate command to list or filter follow-ups.
* `shortcut` without arguments lists all saved shortcuts. To define a shortcut, use both `s/SHORTCUT` and `c/COMMAND_WORD`.
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

### Viewing help: `help`

Shows a message explaining how to access the help page.


Format: `help`

You can also open the help window from the **Help** menu or by pressing `F1`. For the latest instructions about story, follow-up, and shortcut commands, refer to this User Guide.

### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

* All four fields (`n/`, `p/`, `e/`, and `a/`) are required and cannot be blank.
* Names contain only letters, digits, and spaces. A person with exactly the same name already in the address book cannot be added.
* Phone numbers contain at least three digits and no other characters. Emails must have a valid `local-part@domain` format.
* Tag names contain only letters and digits, without spaces.
* Adding a person shows the full contact list.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags, including zero.
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Listing contacts and stories (refactored commands)

PressMemo provides separate commands for displaying both lists, displaying contacts only, or displaying stories only. They also let you filter one list by an entry in the other.

| Command | Effect on CONTACTS | Effect on STORIES |
| --- | --- | --- |
| `list` | Shows all contacts | Shows all stories |
| `listcontacts` | Shows all contacts | Unchanged |
| `listcontacts si/1` | Shows contacts assigned to displayed story 1 | Unchanged |
| `liststories` | Unchanged | Shows all stories |
| `liststories ci/1` | Unchanged | Shows stories assigned to displayed contact 1 |

For the filtered commands, indices are taken from the **currently displayed lists**, not necessarily the full lists. If no contacts or stories match a filter, the relevant panel becomes empty.

### Listing all contacts and stories: `list`

Displays all contacts and stories, removing any active contact and story filters.

Format: `list`

* `list` resets the **CONTACTS** and **STORIES** panels together.
* It does not reset or separately list the follow-ups; those remain visible in the **FOLLOW-UPS** panel.
* `list` has no filtering parameters; extra arguments are ignored. Use `listcontacts` or `liststories` to filter one panel.

Example:
* `find John` followed by `list` restores both lists in full.

### Listing contacts: `listcontacts`

Displays all contacts, or only those assigned to a selected story.

Format: `listcontacts [si/STORY_INDEX]`

* `listcontacts` shows all contacts and removes any active **contact** filter, including a `find` search.
* `listcontacts si/STORY_INDEX` shows only contacts assigned to that story in the **currently displayed story list**.
* The **STORIES** panel remains unchanged. If the selected story has no assigned contacts, the contact list becomes empty.

Examples:
* `liststories` followed by `listcontacts si/1` shows contacts assigned to the first displayed story.
* `listcontacts` shows all contacts again without changing the displayed stories.

### Listing stories: `liststories`

Displays all stories, or only those assigned to a selected contact.

Format: `liststories [ci/CONTACT_INDEX]`

* `liststories` shows all stories and removes any active **story** filter.
* `liststories ci/CONTACT_INDEX` shows only stories assigned to that contact in the **currently displayed contact list**.
* The **CONTACTS** panel remains unchanged. If the selected contact has no assigned stories, the story list becomes empty.

Examples:
* `listcontacts` followed by `liststories ci/1` shows stories assigned to the first displayed contact.
* `find John` followed by `liststories ci/1` uses the first contact in the filtered search results.
* `liststories` shows all stories again without changing the displayed contacts.

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, all of the person's existing tags are removed; adding tags is not cumulative.
* To remove all of a person's tags, enter `t/` without a tag after it.
* The same field restrictions as `add` apply. You cannot change a person's name to another existing person's name.
* Editing shows the full contact list. Editing a contact, including changing their name, preserves their story assignments and updates associated follow-up references.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive; for example, `hans` matches `Hans`.
* Keyword order does not matter; for example, `Hans Bo` matches `Bo Hans`.
* The search considers only names.
* Only full words match; for example, `Han` does not match `Hans`.
* Persons matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.
* The search replaces the current contact filter and searches all contacts. It does not change the story list. Use `listcontacts` to show all contacts again without changing the story list.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`


### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* Deleting a person also removes their assignments from stories and deletes all follow-ups linked to them. The stories themselves remain.

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Adding a story: `addstory`

Adds a story to PressMemo.

Format: `addstory STORY_NAME`

* Enter the name directly after `addstory`, without an `n/` prefix or quotation marks.
* The name must not be blank and can contain only letters, digits, and spaces.
* A story with exactly the same name cannot be added twice. Name comparison is case-sensitive.
* The new story starts with no assigned contacts. Adding it shows the full story list.
* Story editing and adding story tags through commands are not available in this version.

Examples:

* `addstory Housing Investigation`
* `addstory Football article`

### Assigning a contact to a story: `assign`

Links an existing contact to an existing story.

Format: `assign ci/CONTACT_INDEX si/STORY_INDEX`

* Both indices are required and refer to the currently displayed contact and story lists respectively.
* Assign one contact to one story per command. Repeat the command with other indices to create more assignments.
* Assigning a contact to a story they already belong to leaves the existing assignment unchanged.
* After the command, both lists show all entries and the cards show the updated assignments.

Examples:

* `list` and then `assign ci/1 si/1` assigns the 1st contact to the 1st story, provided both lists have entries.
* `assign si/2 ci/1` assigns the 1st displayed contact to the 2nd displayed story.

### Unassigning a contact from a story: `unassign`

Removes the link between a contact and a story, keeping both records.

Format: `unassign ci/CONTACT_INDEX si/STORY_INDEX`

* Both indices are required and refer to the currently displayed contact and story lists respectively.
* Other assignments remain unchanged. Unassigning a contact who is not assigned to that story has no effect on assignments.
* After the command, both lists show all entries and the cards show the updated assignments.

Examples:

* `unassign ci/1 si/1` removes the assignment between the 1st displayed contact and the 1st displayed story.

### Deleting a story: `deletestory`

Deletes the specified story from PressMemo.

Format: `deletestory INDEX`

* The index refers to the number in the displayed story list, including a filtered list, and must be a positive integer.
* Deleting a story also removes its assignments from contacts. The contacts themselves remain.

Examples:

* `liststories` followed by `deletestory 2` deletes the 2nd story in the full story list.
* `liststories ci/1` followed by `deletestory 1` deletes the 1st story assigned to the 1st displayed contact, provided there is a result.

### Adding a follow-up: `addfollowup`

Adds a follow-up reminder linked to an existing contact.

Format: `addfollowup DESCRIPTION ci/CONTACT_INDEX`

* Enter the description as plain text **before** `ci/`; do not use a prefix for it.
* The description must begin with an alphanumeric character, contain only letters, numbers and spaces, and must not be blank.
* `ci/CONTACT_INDEX` refers to the contact's number in the currently displayed contact list. The index must be a positive integer within that list.
* An identical description cannot be added twice for the same contact. Description comparison is case-sensitive.
* Follow-ups belong to contacts, not directly to stories; deleting a story does not remove a contact's follow-ups.
* There is currently no command to edit, search, or separately list follow-ups; view them in the **FOLLOW-UPS** panel.
* Editing a contact's name updates their follow-up references. Deleting a contact also removes their follow-ups.
* New reminders appear in the **FOLLOW-UPS** panel.

Examples:
* `addfollowup Prepare interview questions ci/1`
* `addfollowup Request financial records ci/2`

### Deleting a follow-up: `deletefollowup`

Deletes a follow-up reminder from PressMemo.

Format: `deletefollowup INDEX`

* `INDEX` is a positive integer selecting a follow-up from the follow-up list.
* **Known issue:** The panel sorts follow-ups by contact name, but deletion indexes the underlying model list. If the two orders differ, the numbered item selected by the command may not be the one shown at that number in the panel. See [Known issues](#known-issues).
* Deleting a follow-up does not delete the associated contact.

Examples:
* `deletefollowup 1`
* `deletefollowup 3`

### Managing command shortcuts: `shortcut`

Creates a custom alias for a command or displays all saved aliases.

Format: `shortcut [s/SHORTCUT c/COMMAND_WORD]`

* `shortcut` without arguments lists the existing shortcuts alphabetically, or reports that none are assigned.
* `shortcut s/SHORTCUT c/COMMAND_WORD` assigns an alias to a command.
* A shortcut must start with a lowercase letter and may contain only lowercase letters and digits (e.g., `ls`, `a1`, `ds`).
* Built-in command words cannot be reused as shortcut names.
* Only the following commands can be assigned shortcuts: `add`, `addstory`, `assign`, `clear`, `delete`, `deletestory`, `edit`, `exit`, `find`, `help`, `list`, `liststories`, and `unassign`.
* Other commands, including `listcontacts`, `addfollowup`, `deletefollowup`, `shortcut`, and `unshortcut`, are not supported as shortcut targets in this version.
* Each command can have at most one shortcut, and each shortcut can refer to one command. Reassigning a shortcut or command replaces conflicting mappings.
* Once assigned, type the shortcut in place of the command word and keep any required arguments.
* Shortcuts are saved in `preferences.json` and remain available after restarting the app.

Examples:
* `shortcut` — lists all existing shortcuts.
* `shortcut s/ls c/list` — assigns `ls` to `list`; you can then enter `ls`.
* `shortcut s/a c/add` — assigns `a` to `add`; you can then enter `a n/John Tan p/91234567 e/john@example.com a/Singapore`.
* `shortcut s/ds c/deletestory` — assigns `ds` to `deletestory`.

### Removing a command shortcut: `unshortcut`

Removes an existing custom shortcut without deleting the underlying command.

Format: `unshortcut SHORTCUT`

* `SHORTCUT` must name an existing alias. If it does not exist, an error message is displayed.
* Removing the alias is saved in `preferences.json`.

Examples:
* `unshortcut ls`
* `unshortcut ds`

### Clearing all entries: `clear`

Clears all contacts, stories, assignments, and follow-ups from PressMemo.

Format: `clear`

This affects all records, including those hidden by filters. User preferences and command shortcuts are not removed. There is no confirmation prompt or undo command; back up your data files first if you may need them later.

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

PressMemo automatically saves contact, story, assignment, and follow-up records after every successful command. Custom shortcuts are also saved automatically. You do not need to save manually.

### Editing the data files

PressMemo saves contacts, their story assignments, and follow-up reminders in `data/addressbook.json`. Stories and their contact assignments are saved in `data/stories.json`. User preferences and custom shortcuts are saved in `preferences.json`. These paths are relative to the folder from which you launch the app. If you follow the Quick start instructions, this is the JAR file's folder. The files are loaded on startup.

Advanced users can update records directly by editing these JSON files while the app is closed. Back up all files before editing.

<div markdown="span" class="alert alert-warning"> **Caution:**
If the contact file is invalid or cannot be read, PressMemo starts with an empty contact list. If the story file is invalid or cannot be read, it starts with an empty story list. A missing contact file results in sample contacts; a missing story file results in an empty story list. An invalid file may be overwritten after the next successful command, so restore your backup before running commands.<br>
Furthermore, certain edits can cause PressMemo to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data files only if you are confident that you can update them correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Close PressMemo on both computers. Copy `data/addressbook.json` and `data/stories.json` from your old launch folder to the new one. Copy `preferences.json` too if you want to retain custom shortcuts and GUI settings. Back up existing files before replacing them.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Add follow-up** | `addfollowup DESCRIPTION ci/CONTACT_INDEX`<br> e.g., `addfollowup Interview prep ci/1`
**Add story** | `addstory STORY_NAME`<br> e.g., `addstory Housing Investigation`
**Assign** | `assign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `assign ci/1 si/2`
**Clear all entries** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Delete follow-up** | `deletefollowup INDEX`<br> e.g., `deletefollowup 1`
**Delete story** | `deletestory INDEX`<br> e.g., `deletestory 2`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List all contacts and stories** | `list` (clears both filters)
**List contacts** | `listcontacts [si/STORY_INDEX]`<br> e.g., `listcontacts` (all contacts only) or `listcontacts si/1` (contacts assigned to displayed story 1)
**List stories** | `liststories [ci/CONTACT_INDEX]`<br> e.g., `liststories` (all stories only) or `liststories ci/1` (stories assigned to displayed contact 1)
**Manage shortcuts** | `shortcut [s/SHORTCUT c/COMMAND_WORD]`<br> e.g., `shortcut` or `shortcut s/ls c/list`
**Remove shortcut** | `unshortcut SHORTCUT`<br> e.g., `unshortcut ls`
**Unassign** | `unassign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `unassign ci/1 si/2`
**Help** | `help`
**Exit** | `exit`
