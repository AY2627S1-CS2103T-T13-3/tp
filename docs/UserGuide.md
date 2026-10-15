---
layout: page
title: User Guide
---

PressMemo is a **desktop application for journalists and reporters to manage contacts and stories, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, PressMemo can help you organize sources across multiple stories faster than traditional GUI applications.

This guide describes the features available as of release v1.3.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the PressMemo `.jar` file from [here](https://github.com/AY2627S1-CS2103T-T13-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for PressMemo. The commands below assume the file is named `addressbook.jar`.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI with separate **CONTACTS** and **STORIES** panels should appear in a few seconds. On the first run, the app contains sample contacts and an empty story list.<br>
   The image below is a design mockup. Follow-up reminders and status fields shown in it are not available in this version; `list` shows contacts only.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `addstory Housing Investigation` : Creates a story named `Housing Investigation`.

   * `liststories` : Lists all stories.

   * `assign ci/1 si/1` : Assigns the 1st displayed contact to the 1st displayed story.

   * `list si/1` : Lists the contacts assigned to the 1st displayed story.

   * `delete 1` : Deletes the 1st contact shown in the current list and removes their story assignments.

   * `clear` : Deletes all contacts, stories, and assignments.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

The **CONTACTS** panel shows each person's details, tags, and assigned story names. The **STORIES** panel shows each story's name, assigned contact names, and contact count. A contact can belong to several stories, and a story can have several contacts.


### Notes about the command format:

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.
* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.
* Items followed by `…`​ can appear zero or more times.<br>
  For example, `[t/TAG]…​` may be omitted, or written as `t/friend` or `t/friend t/family`.
* Prefixed parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.
* Indices refer to the numbers in the currently displayed lists, including filtered lists. `ci/CONTACT_INDEX` selects a contact and `si/STORY_INDEX` selects a story. Each index must be a positive integer within the corresponding displayed list. Check the displayed numbers again after changing a list.
* Command words and prefixes are case-sensitive. Use lowercase, such as `addstory`, `ci/`, and `si/`.
* Extraneous parameters for commands that take no parameters, such as `help`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.
* `list` and `liststories` accept only their optional filter shown below; unrelated arguments are rejected.
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.


Format: `help`

You can also open the help window from the **Help** menu or by pressing `F1`. At this commit, the window links to the original AddressBook guide. Use this PressMemo guide for story and assignment commands.

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

### Listing persons: `list`

Shows all persons in the address book, or only those assigned to a particular story.

Format: `list [si/STORY_INDEX]`

* `list` shows all contacts and removes any contact filter, including a `find` search.
* `list si/STORY_INDEX` shows only contacts assigned to the story at that index in the displayed story list.
* The story list is unchanged. If the selected story has no assigned contacts, the contact list is empty.

Examples:

* `liststories` followed by `list si/1` shows contacts assigned to the 1st story in the full story list.
* `list` shows all contacts again.

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, all of the person's existing tags are removed; adding tags is not cumulative.
* To remove all of a person's tags, enter `t/` without a tag after it.
* The same field restrictions as `add` apply. You cannot change a person's name to another existing person's name.
* Editing shows the full contact list. Editing phone, email, address, or tags preserves story assignments. Before changing an assigned person's name, unassign them from their stories, then assign them again after the edit (see [Known issues](#known-issues)).

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
* The search replaces the current contact filter and searches all contacts. It does not change the story list. Use `list` to show all contacts again.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`


### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* Deleting a person also removes their assignments from stories. The stories themselves remain.

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

### Listing stories: `liststories`

Shows all stories, or only those assigned to a particular contact.

Format: `liststories [ci/CONTACT_INDEX]`

* `liststories` shows all stories and removes any story filter.
* `liststories ci/CONTACT_INDEX` shows only stories assigned to the contact at that index in the displayed contact list.
* The contact list is unchanged. If the selected contact has no assigned stories, the story list is empty.

Examples:

* `list` followed by `liststories ci/1` shows stories assigned to the 1st contact in the full contact list.
* `find John` followed by `liststories ci/1` shows stories assigned to the 1st contact in the search results, provided there is a result.
* `liststories` shows all stories again.

### Assigning a contact to a story: `assign`

Links an existing contact to an existing story.

Format: `assign ci/CONTACT_INDEX si/STORY_INDEX`

* Both indices are required and refer to the currently displayed contact and story lists respectively.
* Assign one contact to one story per command. Repeat the command with other indices to create more assignments.
* Assigning a contact to a story they already belong to leaves the existing assignment unchanged.
* After the command, both lists show all entries and the cards show the updated assignments.

Examples:

* `list` followed by `liststories` and then `assign ci/1 si/1` assigns the 1st contact to the 1st story, provided both lists have entries.
* `assign si/2 ci/1` assigns the 1st displayed contact to the 2nd displayed story.

<div markdown="span" class="alert alert-warning">
**Caution:**
Assignments are not saved between runs in this version. After restarting PressMemo, contacts and stories remain, but you must assign them again.
</div>

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

### Clearing all entries: `clear`

Clears all contacts, stories, and assignments from PressMemo.

Format: `clear`

This affects all entries, including those hidden by filters. There is no confirmation prompt or undo command; back up your data files first if you may need them later.

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

PressMemo automatically saves contact and story records after every successful command. You do not need to save manually. Assignments between contacts and stories are not saved in this version and are lost when you close the app.

### Editing the data files

PressMemo saves contacts in `data/addressbook.json` and stories in `data/stories.json`, relative to the folder from which you launch the app. If you follow the Quick start instructions, this is the JAR file's folder. Both files are loaded on startup.

Advanced users can update records directly by editing these JSON files while the app is closed. Back up both files before editing.

<div markdown="span" class="alert alert-warning"> **Caution:**
If the contact file is invalid or cannot be read, PressMemo starts with an empty contact list. If the story file is invalid or cannot be read, it starts with an empty story list. A missing contact file results in sample contacts; a missing story file results in an empty story list. An invalid file may be overwritten after the next successful command, so restore your backup before running commands.<br>
Furthermore, certain edits can cause PressMemo to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data files only if you are confident that you can update them correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Close PressMemo on both computers. Copy both `data/addressbook.json` and `data/stories.json` from your old launch folder to the new one, replacing the destination files if necessary. Back up existing files first. Assignments are not included in these files and must be recreated after launching the app.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. **Assignments are lost after restarting.** Contact and story records are saved, but their links are not. Recreate assignments with `assign` each time you start PressMemo.
4. **Renaming an assigned contact leaves the old name in stories.** This can also make assignment filters omit the renamed contact or their stories. Before renaming, use `unassign` for each of the contact's stories, edit the name, and then use `assign` to restore the links. If already renamed, change the name back first and follow these steps.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Add story** | `addstory STORY_NAME`<br> e.g., `addstory Housing Investigation`
**Assign** | `assign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `assign ci/1 si/2`
**Clear all contacts and stories** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Delete story** | `deletestory INDEX`<br> e.g., `deletestory 2`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List contacts** | `list [si/STORY_INDEX]`<br> e.g., `list` or `list si/1`
**List stories** | `liststories [ci/CONTACT_INDEX]`<br> e.g., `liststories` or `liststories ci/1`
**Unassign** | `unassign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `unassign ci/1 si/2`
**Help** | `help`
**Exit** | `exit`
