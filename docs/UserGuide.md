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

2. Download the PressMemo `.jar` file from [here](https://github.com/AY2627S1-CS2103T-T13-3/tp/releases).

3. Copy the file to a folder you can write to as the _home folder_ for PressMemo.

4. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar pressmemo.jar`.<br>
   A GUI with the **CONTACTS**, **STORIES**, and **FOLLOW-UPS** panels similar to the one below should appear in a few seconds. On the first run, the app contains sample contacts, stories and follow-ups.<br>
   ![Ui](images/QuickstartUi.png)

5. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

    * `list` : Displays all contacts, stories and follow-ups.

    * `addcontact n/Jane Smith p/91234567 e/janesmith@example.com a/Jane street, block 987, #10-10` : Adds a contact named `Jane Smith` to PressMemo.

    * `assign ci/1 si/2` : Assigns the 1st displayed contact to the 2nd displayed story.

    * `deletefollowup 1` : Deletes the 1st displayed follow-up.

    * `shortcut s/ls c/list` : Defines `ls` as a shortcut for `list`.

    * `clear` : Deletes all contacts, stories and follow-ups.

    * `exit` : Exits the app.

6. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------
## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `addcontact n/NAME`, replace `NAME` with a value such as `John Doe`.
* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.
* Items followed by `…`​ can appear zero or more times.<br>
  For example, `[t/TAG]…​` may be omitted, or written as `t/friend` or `t/friend t/family`.
* Prefixed parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.
* Extraneous parameters for commands that take no parameters, such as `help`, `exit`, `clear`, and `list`, are not allowed.<br>
  For example, `help 123` will result in an error.
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Common input types

| Input type        | Definition                                                                | Format/s                                                                                                                                                                                       | Examples                                      |
|-------------------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| `user-string`     | A sequence of characters.                                                 | Must contain one or more characters, with at least one non-space character.<br><br>Any forward slash must be escaped using backslash (\\).<br><br>Any spaces at the start and end are ignored. | wumpus<br><br>wumpus farts<br><br>date 3\\/12 |
| `word`            | A `user-string` that does not contain spaces or forward slashes.          | Must not contain spaces or forward slashes                                                                                                                                                     | wumpus<br><br>wumpus,farts!                   |
| `contact-index`   | A number representing the position of a `Contact` in the contact panel.   | Integer between 1 and the number of `Contact`s in the contact panel<br><br>If the contact panel is empty, there is no valid `contact-index`                                                    | 1                                             |
| `story-index`     | A number representing the position of a `Story` in the story panel.       | Integer between 1 and the number of `Story`s in the story panel<br><br>If the story panel is empty, there is no valid `story-index`                                                            | 2                                             |
| `followup-index`  | A number representing the position of a `Followup` in the followup panel. | Integer between 1 and the number of `Followup`s in the followup panel<br><br>If the followup panel is empty, there is no valid `followup-index`                                                | 1                                             |

### Viewing help: `help`

Toggles the help panel which lists all default commands and their formats. 

Format: `help`

![Ui with Help Panel](images/UiWithHelpPanel.png)

* Enter `help` to open or close the help panel. The link to the user guide will also be shown in the message panel. Alternatively, use the **Help** menu at the top. 
* Use the scrollbar or select the command panel and use the up and down arrow keys to view the command list.
* The help panel displays only the default commands and does not include any custom shortcuts you have created. 
* For the latest instructions about contact, story, follow-up, and shortcut commands, refer to this User Guide.

### Adding a contact: `addcontact`

Adds a contact to PressMemo.

Format: `addcontact n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

* All four fields (`n/`, `p/`, `e/`, and `a/`) are required and cannot be blank or start with whitespace.
* Contact names must be unique. A contact with exactly the same name already in PressMemo cannot be added.
* Forward slashes (`/`) within values must be escaped with a backslash (`\/`).
* A contact can have any number of tags, including zero. Tag names must not be blank or start with whitespace.
* Adding a contact preserves any active contact filter. The new contact will appear in the contact list if it matches the active filter.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A contact can have any number of tags, including zero.
</div>

Examples:
* `addcontact n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `addcontact n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

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

<div markdown="block" class="alert alert-info">

**:information_source: Visual Filter Status Indicators:**<br>

Each panel header (**CONTACTS**, **STORIES**, and **FOLLOW-UPS**) displays an active filter indicator badge next to the panel header:
* **`[All]`** (subtle badge): Indicates the panel displays all items without any filter applied.
* **`[Filtered: "..."]`** or **`[Filtered by ...: "..."]`** (vibrant amber badge): Indicates a filter or search query is currently active on that panel (e.g., `[Filtered by story: "Investigation"]` or `[Filtered: "John"]`).
* Hovering your mouse over the badge displays the complete filter description as a tooltip if the text is truncated on narrow windows.

This visual badge ensures you always know whether the displayed index numbers (e.g., `1`, `2`, `3`) correspond to the complete list or a filtered subset, eliminating index ambiguity!
</div>

### Listing all contacts and stories: `list`

- **Objective:** Displays all contacts and stories, removing any active contact and story filters.
![list](images/list-example.png)

#### Usage / Command Format

`list`

#### Parameters & Constraints

This command does not accept parameters. Any extraneous characters will result in an error.

> **Tip / Note:**
> * `list` resets both the **CONTACTS** and **STORIES** panels together.
> * It does not reset or separately filter follow-ups; those remain visible in the **FOLLOW-UPS** panel.
> * Use `listcontacts` or `liststories` if you want to filter or reset only one panel.

#### Examples

* **Example 1: Reset all active contact and story filters**
  * **Input / Action:** `list`
  * **Expected Outcome:** Both **CONTACTS** and **STORIES** panels display all entries, and both panel header filter badges update to `[All]`.

#### Common Errors & Troubleshooting

| Error Message / Symptom | Cause | How to Fix |
| :--- | :--- | :--- |
| `This command does not accept parameters.` | Extraneous text was entered after the command word. | Remove any arguments and run `list` alone. |

### Listing contacts: `listcontacts`

- **Objective:** Displays all contacts, or only contacts assigned to a selected story.
![listcontacts](images/listcontacts-example.png)

#### Usage / Command Format

`listcontacts [si/STORY_INDEX]`

#### Parameters & Constraints

| Parameter        | Type          | Description                                                                          | Examples |
| :--------------- | :------------ | :----------------------------------------------------------------------------------- | :------- |
| `si/STORY_INDEX` | `story-index` | (Optional) Story to filter contacts by, from the displayed **STORIES** panel        | `1`      |

> **Tip / Note:**
> * `listcontacts` without parameters displays all contacts and clears any active contact filter (including `findcontact`).
> * `listcontacts si/STORY_INDEX` displays only contacts assigned to that story in the **currently displayed story list**.
> * The **STORIES** panel remains unchanged. If the selected story has no assigned contacts, the contact list becomes empty.
> * The contact panel header filter badge updates dynamically (e.g. `[All]` or `[Filtered by story: "..."]`).

#### Examples

* **Example 1: View all contacts**
  * **Input / Action:** `listcontacts`
  * **Expected Outcome:** All contacts in PressMemo are displayed in the **CONTACTS** panel. The **STORIES** and **FOLLOW-UPS** panels remain unchanged.

* **Example 2: Filter contacts by assigned story**
  * **Input / Action:** `listcontacts si/1`
  * **Expected Outcome:** Displays only contacts assigned to the first story currently displayed in the **STORIES** panel.

#### Common Errors & Troubleshooting

| Error Message / Symptom | Cause | How to Fix |
| :--- | :--- | :--- |
| `The story index 100 is invalid for the displayed list of 9 story(ies).` | The provided story index is greater than the number of displayed stories (or the panel is empty). | Check the index numbers displayed in the **STORIES** panel or run `liststories` to view all stories. |
| `Index must be a positive integer.` | The story index is not a positive integer. | Ensure the index is a positive whole number (e.g. `1`, `2`). |
| `The following prefix(es) should not be duplicated: si/` | Prefix `si/` was entered multiple times in the same command. | Specify at most one `si/` prefix. |

### Listing stories: `liststories`

Displays all stories, or only those assigned to a selected contact.

Format: `liststories [ci/CONTACT_INDEX]`

* `liststories` shows all stories and removes any active **story** filter.
* `liststories ci/CONTACT_INDEX` shows only stories assigned to that contact in the **currently displayed contact list**.
* The **CONTACTS** panel remains unchanged. If the selected contact has no assigned stories, the story list becomes empty.

Examples:
* `listcontacts` followed by `liststories ci/1` shows stories assigned to the first displayed contact.
* `findcontact John` followed by `liststories ci/1` uses the first contact in the filtered search results.
* `liststories` shows all stories again without changing the displayed contacts.

### Editing a contact: `editcontact`

- **Objective:** Edits the details of an existing contact in PressMemo.
![editcontact](images/editcontact-example.png)

#### Usage / Command Format

`editcontact INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

#### Parameters & Constraints

| Parameter   | Type            | Description                                                                                     | Examples            |
| :---------- | :-------------- | :---------------------------------------------------------------------------------------------- | :------------------ |
| `INDEX`     | `contact-index` | Contact to edit, from the displayed **CONTACTS** panel                                          | `1`                 |
| `n/NAME`    | `user-string`   | New name of the contact (must be unique; cannot be blank or start with whitespace)             | Betsy Crower        |
| `p/PHONE`   | `user-string`   | New phone number (cannot be blank or start with whitespace)                                     | 91234567            |
| `e/EMAIL`   | `user-string`   | New email address (cannot be blank or start with whitespace)                                    | johndoe@example.com |
| `a/ADDRESS` | `user-string`   | New physical address (cannot be blank or start with whitespace; forward slashes must be escaped) | 123 Clementi Rd     |
| `t/TAG`     | `user-string`   | New tag to assign (can be repeated, or `t/` to clear tags; replaces all existing tags)          | friend              |

> **Tip / Note:**
> * `INDEX` must be a positive integer within the displayed **CONTACTS** panel.
> * At least one optional field must be provided.
> * Existing values are updated to the specified input values; unmentioned fields remain unchanged.
> * **Tag Replacement Limitation & Workaround:** When editing tags with `t/TAG`, all existing tags on the contact are replaced rather than appended. For example, if a contact at index 1 currently has tags `friends` and `owesMoney`, executing `editcontact 1 t/source` removes both existing tags and leaves only `source`. To add a new tag while preserving existing ones, you must re-enter all existing tags alongside the new tag (e.g. `editcontact 1 t/friends t/owesMoney t/source`).
> * *Planned Improvement:* We recognize this current design requires re-typing existing tags when adding new ones. A future enhancement is planned to support adding and removing tags incrementally without overwriting existing tags.
> * To remove all tags from a contact, enter `t/` without a tag name after it.
> * Editing a contact's name preserves all story assignments and updates the contact name across linked stories and follow-ups.
> * Editing preserves the active contact list filter instead of resetting to all contacts.

#### Examples

* **Example 1: Edit phone number and email address**
  * **Input / Action:** `editcontact 1 p/91234567 e/johndoe@example.com`
  * **Expected Outcome:** Edits the phone number and email address of the 1st contact to `91234567` and `johndoe@example.com` respectively.

* **Example 2: Edit name and clear all existing tags**
  * **Input / Action:** `editcontact 2 n/Betsy Crower t/`
  * **Expected Outcome:** Changes the name of the 2nd contact to `Betsy Crower` and clears all existing tags from that contact.

* **Example 3: Add a new tag while retaining existing tags**
  * **Input / Action:** `editcontact 1 t/friends t/owesMoney t/source`
  * **Expected Outcome:** Replaces the tags with `friends`, `owesMoney`, and `source`, successfully adding `source` while keeping the original two tags.

#### Common Errors & Troubleshooting

| Error Message / Symptom | Cause | How to Fix |
| :--- | :--- | :--- |
| `At least one field to edit must be provided.` | No fields (`n/`, `p/`, `e/`, `a/`, `t/`) were specified after the index. | Provide at least one field to update. |
| `Error: there is already a contact named "Alex Yeoh"!` | The edited name duplicates another existing contact in PressMemo. | Use a unique contact name. |
| `The contact index 5 is invalid for the displayed list of 2 contact(s).` | The provided index is greater than the number of contacts displayed (or the list is empty). | Check the index numbers displayed in the **CONTACTS** panel or run `listcontacts` to view all contacts. |
| `Index must be a positive integer.` | The index provided is zero, negative, or not a whole number. | Provide a positive integer index (e.g. `1`, `2`). |
| `Phone numbers should not be blank or start with whitespace` | The phone number is blank or begins with whitespace. | Enter a phone number that does not start with whitespace. |
| `Emails should not be blank or start with whitespace` | The email address is blank or begins with whitespace. | Enter an email address that does not start with whitespace. |
| `Contact names should not be blank or start with whitespace` | The contact name is blank or begins with whitespace. | Enter a name that does not start with whitespace. |
| `Addresses should not be blank or start with whitespace` | The address is blank or begins with whitespace. | Enter an address that does not start with whitespace. |
| `Tag names should not be blank or start with whitespace` | A tag name begins with a space or contains only whitespace after `t/`. | Enter a non-blank tag name without leading whitespace (or use `t/` without spaces to clear tags). |
| `Unrecognised prefix or unescaped forward slash in "a/b". To use a forward slash as text, escape it with a backslash, e.g. "\/".` | An unescaped forward slash was entered or an unrecognized prefix was used. | Escape any forward slash intended as text with `\/`. |
| `The following prefix(es) should not be duplicated: n/` | A single-value prefix (`n/`, `p/`, `e/`, `a/`) was provided multiple times. | Specify each single-value prefix only once. |

### Locating contacts by name: `findcontact`

Finds contacts whose names contain any of the given keywords.

Format: `findcontact KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive; for example, `hans` matches `Hans`.
* Keyword order does not matter; for example, `Hans Bo` matches `Bo Hans`.
* The search considers only names.
* Only full words match; for example, `Han` does not match `Hans`.
* Contacts matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.
* The search replaces the current contact filter and searches all contacts. It does not change the story list. Use `listcontacts` to show all contacts again without changing the story list.

Examples:
* `findcontact John` returns `john` and `John Doe`
* `findcontact alex david` returns `Alex Yeoh`, `David Li`


### Deleting a contact: `deletecontact`

Deletes the specified contact from PressMemo.

Format: `deletecontact INDEX`

* Deletes the contact at the specified `INDEX`.
* The index refers to the index number shown in the displayed contact list.
* The index **must be a positive integer** 1, 2, 3, …​
* Deleting a contact also removes their assignments from stories and deletes all follow-ups linked to them. The stories themselves remain.

Examples:
* `list` followed by `deletecontact 2` deletes the 2nd contact in PressMemo.
* `findcontact Betsy` followed by `deletecontact 1` deletes the 1st contact in the results of the `findcontact` command.

## Adding a story: `addstory`

- **Objective:** Adds a story to PressMemo
![addstory](images/addstory-example.png)

### Usage / Command Format

`addstory STORY_NAME [t/TAG]…`

### Parameters

| Parameter    | Type          | Description              | Examples                  |
| :----------- | :------------ | :----------------------- | :------------------------ |
| `STORY_NAME` | `user string` | name of the story to add | wumpus<br><br>find wumpus |
| TAG          | `user string` | a tag for the story      | smelly                    |


> **Tip / Note:**
> * No two stories can have the same name. The provided `STORY_NAME` must not already exist in PressMemo.
> * `addstory` preserves any active story filter; the new story will appear in the story panel if it matches the active filter.
> * The new story starts with no assigned contacts.

### Examples

* **Example 1: valid and unique story name, no tags 
  * **Input / Action:** `addstory find wumpus`
  * **Expected Outcome:** The story named "find wumpus" is added to the stories in PressMemo and displayed in the story panel without tags

* **Example 2: valid and unique story name with 2 tags 
  * **Input / Action:** `addstory find wumpus t/smelly t/at 2, 2`
  * **Expected Outcome:** The story named "find wumpus" is added to the stories in PressMemo and displayed in the story panel with the tags "smelly" and "at 2, 2"

### Common Errors & Troubleshooting

| Error Message / Symptom                     | Cause                                                                    | How to Fix                                                                              |
| :------------------------------------------ | :----------------------------------------------------------------------- | :-------------------------------------------------------------------------------------- |
| Error: there is already a story named {xxx} | Trying to add a story named {xxx} when {xxx} already exists in PressMemo | delete the existing story named {xxx} first<br><br>OR<br><br>use a different story name |

## Filter stories by name: `findstories`

- **Objective:** Finds stories whose names contain any of the given keywords (case-insensitive) and displays them in the story panel.
![findstories](images/findstories-example.png)

### Usage / Command Format

`findstories STORY_NAME [MORE_KEYWORDS]...`

### Parameters & Constraints

| Parameter       | Type   | Description                                      | Examples   |
| :-------------- | :----- | :----------------------------------------------- | :--------- |
| `STORY_NAME`    | `word` | First keyword to search for in story names       | wumpus     |
| `MORE_KEYWORDS` | `word` | Additional keywords to search for in story names | the smelly |

> **Tip / Note:**
> * `findstories` is case-insensitive (e.g., `wumpus` matches `WumPuS`).
> * Only full words will be matched (e.g., `wump` will not match `wumpus`).
> * The order of the keywords does not matter.
> * All stories matching at least one keyword are returned (e.g. `wumpus smelly` matches `find wumpus` and `something smelly`) 
> * The **STORIES** panel is updated without changing the **CONTACTS** or **FOLLOW-UPS** panels.
> * To display all stories again, use `liststories` or `list`.

### Examples

* **Example 1: Single keyword search**
  * **Input / Action:** `findstories wumpus`
  * **Expected Outcome:** Displays all stories with `wummpus` as a word in their name. A message in the console indicates the number of matches found.

* **Example 2: Multiple keywords search**
  * **Input / Action:** `findstories wumpus smelly`
  * **Expected Outcome:** Displays all stories whose names contain either `wumpus` or `smelly`. A message in the console indicates the number of matches found.

* **Example 3: Search with no matches**
  * **Input / Action:** `findstories wompusDoesNotExist` when there is no story whose name contains `wompusDoesNotExist`
  * **Expected Outcome:** The story panel displays no stories. A message in the console indicates that no matches were found.

### Common Errors & Troubleshooting

| Error Message / Symptom                                    | Cause                                                                           | How to Fix                                                                   |
| :--------------------------------------------------------- | :------------------------------------------------------------------------------ | :--------------------------------------------------------------------------- |
| `0 story(ies) listed!` when matching stories were expected | Search keywords were partial words (e.g., `wump` instead of `wumpus`)           | Ensure full words are used in the search keyword, e.g., `findstories wumpus` |

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

## Deleting a story: `deletestory`

- **Objective:** Deletes the specified story from PressMemo.
![deletestory](images/deletestory-example.png)

### Usage / Command Format

`deletestory INDEX`

### Parameters & Constraints

| Parameter | Type          | Description                                                        | Examples |
| :-------- | :------------ | :----------------------------------------------------------------- | :------- |
| `INDEX`   | `Story Index` | 1-based index of the story to delete from the displayed story list | `1`      |

> **Tip / Note:**
> * Deleting a story automatically unassigns it from all contacts originally assigned to it. The contacts themselves remain in PressMemo.
> * Follow-ups are unaffected.
> * Deletion cannot be undone.

### Examples

* **Example 1: Deleting a story from the full story list**
  * **Input / Action:** `deletestory 2`
  * **Expected Outcome:** Deletes the 2nd story from the story panel. Identifying information about the recently-deleted story is displayed in the console.

### Adding a follow-up: `addfollowup`

- **Objective:** Adds a follow-up reminder and links it to an existing contact.

#### Usage / Command Format

`addfollowup DESCRIPTION ci/CONTACT_INDEX`

#### Parameters & Constraints

| Parameter          | Type            | Description                                                            | Examples                    |
| :----------------- | :-------------- | :--------------------------------------------------------------------- | :-------------------------- |
| `DESCRIPTION`      | `user-string`   | Description of the follow-up                                           | Prepare interview questions |
| `ci/CONTACT_INDEX` | `contact-index` | Contact to link the follow-up to, from the displayed **CONTACTS** panel | `1`                         |

> **Tip / Note:**
> * Enter `DESCRIPTION` as plain text before `ci/`; it does not use a prefix.
> * `DESCRIPTION` cannot be blank. Leading and trailing whitespace is removed.
> * A contact cannot have two follow-ups with exactly the same description.
>   * Description comparison is case-sensitive.
> * Follow-ups belong to contacts, not directly to stories.
>   * Deleting a story does not remove a contact's follow-ups.
>   * Each follow-up belongs to exactly one contact.
> * Editing a contact's name updates the contact name shown on their follow-ups.
> * Deleting a contact also deletes all follow-ups linked to them.
> * Adding a follow-up retains the **FOLLOW-UPS** panel filter. New follow-ups are displayed if they match the existing filter.

#### Examples

* **Example 1: Add a follow-up to the first displayed contact**
  * **Input / Action:** `addfollowup Prepare interview questions ci/1`
  * **Expected Outcome:** Adds the follow-up `Prepare interview questions` and links it to the first contact in the displayed **CONTACTS** panel.

#### Common Errors & Troubleshooting

| Error Message / Symptom                                                                 | Cause                                                                                                   | How to Fix                                                        |
|:----------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------| :---------------------------------------------------------------- |
| `Error: there is already a followup "Check back for interview" for the contact "Alex"!` | The contact (Alex) already has a follow-up with exactly the same description (Check back for interview) | Use a different description or edit the existing follow-up        |

### Editing a follow-up: `editfollowup`

- **Objective:** Updates the description or linked contact of an existing follow-up.

#### Usage / Command Format

`editfollowup INDEX [d/DESCRIPTION] [ci/CONTACT_INDEX]`

#### Parameters & Constraints

| Parameter          | Type             | Description                                                                     | Examples      |
| :----------------- | :--------------- | :------------------------------------------------------------------------------ | :------------ |
| `INDEX`            | `followup-index` | Follow-up to edit, from the displayed **FOLLOW-UPS** panel                       | `1`           |
| `d/DESCRIPTION`    | `user-string`    | New follow-up description                                                       | Call tomorrow |
| `ci/CONTACT_INDEX` | `contact-index`  | New linked contact, from the displayed **CONTACTS** panel                        | `2`           |

> **Tip / Note:**
> * At least one of `d/DESCRIPTION` or `ci/CONTACT_INDEX` must be provided.
> * A follow-up can be linked to only one contact at a time.
>   * Linking it to a new contact replaces the existing link.
> * Existing values are preserved for fields that are not provided.
> * The same description constraints as `addfollowup` apply.
> * The edited follow-up cannot duplicate another follow-up linked to the same contact.
> * The current follow-up filter on **FOLLOW-UPS** panel is preserved. If the edited follow-up no longer matches that filter, it disappears from the displayed **FOLLOW-UPS** panel but remains saved in PressMemo.

#### Examples

* **Example 1: Edit only the description**
  * **Input / Action:** `editfollowup 1 d/Call tomorrow`
  * **Expected Outcome:** Changes the description of the first displayed follow-up to `Call tomorrow` while preserving its linked contact.

* **Example 2: Edits both the description and linked contact**
  * **Input / Action:** `editfollowup 1 d/Call tomorrow ci/2`
  * **Expected Outcome:** Changes the first displayed follow-up's description to `Call tomorrow` and replaces the linked contact with the second displayed contact.

#### Common Errors & Troubleshooting

| Error Message / Symptom                                                              | Cause                                                                                 | How to Fix                                                  |
|:-------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------|:------------------------------------------------------------|
| `At least one field to edit must be provided.`                                       | Neither `d/DESCRIPTION` nor `ci/CONTACT_INDEX` was supplied                           | Provide at least one field to update                        |
| `Error: there is already a followup "Test" for the contact "Alex"!`                  | The edited followup is a duplicate of another follow-up for the linked contact (Alex) | Use a different description or contact                      |
| `Follow-up descriptions should not be blank or start with whitespace`                | `DESCRIPTION` is blank                                                                | Enter a non-blank description |

### Deleting a follow-up: `deletefollowup`

- **Objective:** Deletes the specified follow-up reminder from PressMemo.

#### Usage / Command Format

`deletefollowup INDEX`

#### Parameters & Constraints

| Parameter | Type             | Description                                               | Examples |
| :-------- | :--------------- | :-------------------------------------------------------- | :------- |
| `INDEX`   | `followup-index` | Follow-up to delete from the displayed **FOLLOW-UPS** panel | `1`      |

> **Tip / Note:**
> * `INDEX` must be a positive integer within the displayed follow-up list.
> * Deleting a follow-up does not delete its linked contact.
> * Deletion cannot be undone.

#### Examples

* **Example 1: Delete from the full follow-up list**
  * **Input / Action:** `listfollowups`, followed by `deletefollowup 1`
  * **Expected Outcome:** Deletes the first follow-up displayed in the **FOLLOW-UPS** panel.

* **Example 2: Delete from filtered search results**
  * **Input / Action:** `findfollowups interview`, followed by `deletefollowup 1`
  * **Expected Outcome:** Deletes the first follow-up displayed in the filtered **FOLLOW-UPS** panel.

### Finding follow-ups by description: `findfollowups`

- **Objective:** Finds follow-ups whose descriptions contain any of the given keywords and displays them in the **FOLLOW-UPS** panel.

#### Usage / Command Format

`findfollowups KEYWORD [MORE_KEYWORDS]...`

#### Parameters & Constraints

| Parameter       | Type   | Description                                                 | Examples |
| :-------------- | :----- | :---------------------------------------------------------- |:---------|
| `KEYWORD`       | `word` | First keyword to search for in follow-up descriptions       | interview |
| `MORE_KEYWORDS` | `word` | Additional keywords to search for in follow-up descriptions | meeting  |

> **Tip / Note:**
> * The search is case-insensitive; for example, `interview` matches `Interview`.
> * Only complete whitespace-separated words match; for example, `inter` does not match `interview`.
> * Punctuation is treated as part of a word. For example, `interview` does not match `interview,`, but `interview,` does.
> * Keyword order does not matter.
> * Follow-ups matching at least one keyword are returned (an `OR` search).
> * The search considers only follow-up descriptions, not linked contact names.
> * The search replaces the current follow-up filter and searches all saved follow-ups. The **CONTACTS** and **STORIES** panels remain unchanged.
> * Use `listfollowups` to display all follow-ups again.

#### Examples

* **Example 1: Search using one keyword**
  * **Input / Action:** `findfollowups interview`
  * **Expected Outcome:** Displays follow-ups containing the full word `interview` in their descriptions.

* **Example 2: Search using multiple keywords**
  * **Input / Action:** `findfollowups interview meeting`
  * **Expected Outcome:** Displays follow-ups whose descriptions contain either `interview` or `meeting`.

* **Example 3: Search with no matches**
  * **Input / Action:** `findfollowups nonexistentkeyword`
  * **Expected Outcome:** Displays an empty **FOLLOW-UPS** panel and reports `0 followup(s) listed!`.
    * There are no follow-ups whose descriptions contain `nonexistentkeyword`.

#### Common Errors & Troubleshooting

| Error Message / Symptom                                         | Cause                                                | How to Fix                                                        |
| :-------------------------------------------------------------- | :--------------------------------------------------- | :---------------------------------------------------------------- |
| `0 followup(s) listed!` when matches were expected              | The search used a partial word, mismatched punctuation, or the wrong keywords | Use complete whitespace-separated words, including their punctuation, from the follow-up descriptions |
| `Invalid command format.`                                       | No search keyword was supplied                       | Provide at least one keyword, e.g., `findfollowups interview`     |

## Listing follow-ups: `listfollowups`

- **Objective:** Displays follow-ups in the follow-ups panel, optionally filtering them by assigned contact.
![listfollowups](images/listfollowups-example.png)

### Usage / Command Format

`listfollowups [ci/CONTACT_INDEX]`

### Parameters & Constraints

| Parameter          | Type            | Description                                       | Examples |
| :----------------- | :-------------- | :------------------------------------------------ | :------- |
| `ci/CONTACT_INDEX` | `Contact Index` | to display only follow-ups linked to this contact | 1        |

> **Tip / Note:**
> * If `ci/CONTACT_INDEX` is omitted, `listfollowups` displays all follow-ups in PressMemo.
> * If `ci/CONTACT_INDEX` is provided, `listfollowups` displays only follow-ups linked to that contact.
> * `listfollowups` only updates the **FOLLOW-UPS** panel; the **CONTACTS** and **STORIES** panels remain unchanged.

### Examples

* **Example 1: List all follow-ups**
  * **Input / Action:** `listfollowups`
  * **Expected Outcome:** All follow-ups from PressMemo are displayed in the **FOLLOW-UPS** panel.

* **Example 2: List follow-ups for a specific contact**
  * **Input / Action:** `listfollowups ci/1`
  * **Expected Outcome:** Displays only follow-ups linked to the first displayed contact in the **FOLLOW-UPS** panel. 

### Managing command shortcuts: `shortcut`

- **Objective:** Displays all saved command shortcuts or assigns a custom 1-to-1 alias to a PressMemo command.
![shortcut](images/shortcut-example.png)

#### Usage / Command Format

`shortcut [s/SHORTCUT c/COMMAND_WORD]`

#### Parameters & Constraints

| Parameter        | Type          | Description                                                                                     | Examples |
| :--------------- | :------------ | :---------------------------------------------------------------------------------------------- | :------- |
| `s/SHORTCUT`     | `user-string` | (Optional) Custom shortcut alias (must contain only lowercase letters and digits)               | `ls`     |
| `c/COMMAND_WORD` | `word`        | (Optional) Built-in command word to map to (must be an assignable command)                      | `list`   |

> **Tip / Note:**
> * `shortcut` without arguments lists all existing shortcuts in alphabetical order.
> * `shortcut s/SHORTCUT c/COMMAND_WORD` assigns a 1-to-1 alias between the shortcut and the command word.
> * A shortcut must start with a lowercase letter and contain only lowercase letters and digits (e.g., `ls`, `a1`, `ds`). Shortcuts cannot contain whitespace or forward slashes.
> * Built-in command words cannot be used as shortcut names.
> * Supported assignable commands: `addcontact`, `addstory`, `assign`, `clear`, `deletecontact`, `deletestory`, `editcontact`, `exit`, `findcontact`, `findstory`, `help`, `list`, `liststories`, and `unassign`.
> * Other commands (`listcontacts`, `addfollowup`, `deletefollowup`, `shortcut`, `unshortcut`) cannot be assigned shortcuts.
> * Each command can have at most one shortcut, and each shortcut can map to at most one command. Assigning an existing command to a new shortcut automatically replaces the old mapping.
> * Shortcuts are saved in `preferences.json` and persist across application restarts.

#### Examples

* **Example 1: List all existing shortcuts**
  * **Input / Action:** `shortcut`
  * **Expected Outcome:** Displays all active shortcuts formatted alphabetically (e.g., `Available shortcuts:\nls -> list`).

* **Example 2: Assign a new shortcut**
  * **Input / Action:** `shortcut s/ls c/list`
  * **Expected Outcome:** Assigns shortcut `ls` to `list`. You can now enter `ls` in place of `list`.

#### Common Errors & Troubleshooting

| Error Message / Symptom | Cause | How to Fix |
| :--- | :--- | :--- |
| `The shortcut "list" is a built-in command and cannot be reassigned.` | The shortcut matches a built-in command word. | Choose a shortcut alias that is not a built-in command word. |
| `The shortcut "my ls" cannot contain whitespace or forward slashes.` | The shortcut contains spaces or forward slashes. | Use only lowercase letters and digits without spaces or `/`. |
| `Unknown command: invalidcmd.` | The command word is not recognized or not assignable. | Provide a valid assignable PressMemo command word. |
| `The following prefix(es) should not be duplicated: s/` | Prefix `s/` or `c/` was specified multiple times. | Specify each prefix only once. |

### Removing a command shortcut: `unshortcut`

- **Objective:** Removes an existing custom shortcut alias without affecting the underlying command.
![unshortcut](images/unshortcut-example.png)

#### Usage / Command Format

`unshortcut SHORTCUT`

#### Parameters & Constraints

| Parameter  | Type          | Description                                    | Examples |
| :--------- | :------------ | :--------------------------------------------- | :------- |
| `SHORTCUT` | `user-string` | Existing shortcut alias to remove from PressMemo | `ls`     |

> **Tip / Note:**
> * `SHORTCUT` must match an existing assigned shortcut alias.
> * Removing the alias is saved in `preferences.json` immediately.

#### Examples

* **Example 1: Remove an assigned shortcut**
  * **Input / Action:** `unshortcut ls`
  * **Expected Outcome:** Displays `Removed shortcut: ls -> list`. Typing `ls` will now report an unknown command.

#### Common Errors & Troubleshooting

| Error Message / Symptom | Cause | How to Fix |
| :--- | :--- | :--- |
| `The shortcut "ls" is not assigned to any command.` | The specified shortcut does not exist. | Run `shortcut` to view all active shortcuts, then verify the shortcut name. |

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

PressMemo saves contacts, stories, their assignments, and follow-up reminders together in `data/addressbook.json`. User preferences and custom shortcuts are saved in `preferences.json`. These paths are relative to the folder from which you launch the app. If you follow the Quick start instructions, this is the JAR file's folder. The files are loaded on startup.

Advanced users can update records directly by editing these JSON files while the app is closed. Back up all files before editing.

<div markdown="span" class="alert alert-warning"> **Caution:**
If the data file is invalid or cannot be read, PressMemo starts with empty contact, story, and follow-up lists. A missing data file results in sample data. An invalid file may be overwritten after the next successful command, so restore your backup before running commands.<br>
Furthermore, certain edits can cause PressMemo to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data files only if you are confident that you can update them correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## Common Issues and Troubleshooting

| Error Messages / Symptoms                                                                                                                                                                        | Causes                                                                                                                                                                 | How to Fix                                                                                                                                                       |
| :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| <br>`The story index 100 is invalid for the displayed list of 9 story(ies).`<br>                                                                                                                 | The Contact Index, Story Index, or Followup Index provided is greater than the number of items currently displayed in the corresponding panel (or the panel is empty). | Check the index numbers displayed in the corresponding panel (Contacts, Stories, or Follow-ups). Run `list`, to show all items, if the item you need is missing. |
| `Index must be a positive integer.`                                                                                                                                                              | The Contact Index, Story Index, or Followup Index is not a positive integer.                                                                                           | Ensure the index is a positive integer with no signs, decimals, or letters.                                                                                      |
| `Unrecognised prefix or unescaped forward slash in "a/b". To use a forward slash as text, escape it with a backslash, e.g. "\/".`<br><br>`The following prefix(es) should not be duplicated: n/` | The provided text contains an unrecognised prefix or unescaped forward slash.                                                                                          | Only use supported prefixes, and escape forwad slashes intended to be part of a `user-string` (using \\/)                                                        |
| `This command does not accept parameters.`                                                                                                                                                       | Unexpected text was entered after the command word (and before any prefixes).                                                                                          | Remove the extraneous text after the command word (and before any prefixes).                                                                                     |
| `Missing required input: STORY_NAME.`                                                                                                                                                            | The command expects text input after the command word (and before any prefixes), but none was entered.                                                                 | Provide the required preamble text immediately after the command word (and before any prefixes).                                                                 |
| `The following prefix(es) should not be duplicated: n/`                                                                                                                                          | A prefix was provided multiple times in the same command, even though at most one is supported.                                                                        | Remove duplicate prefixes.                                                                                                                                       |
| `Missing required input: ci/.`                                                                                                             | One or more compulsory prefixes were omitted                                                                                                                           | Include all mandatory prefixes and their values as indicated in the command usage message.                                                                       |
| `Unknown command: womp.`                                                                                                                                                                         | The `command-word` was mistyped, or uses incorrect casing.<br>`command-word`a are case-sensitive.                                                                      | Enter a valid `command-word` or shortcut.<br><br>Type `help` to view all available commands.<br><br>Type `shortcut` to view all available shortcuts.             |
| `Data file at data\addressbook.json could not be loaded. Will be starting with an empty PressMemo.                                                                                               | The data file's contents are corrupted, or PressMemo does not have permission to read the file.                                                                        | Avoid editing the data file directly unless you know what you are doing.<br><br>Make sure you give PressMemo permissions to read and write the data file.<br>    |

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Close PressMemo on both computers. Copy `data/addressbook.json` from your old launch folder to the new one. Copy `preferences.json` too if you want to retain custom shortcuts and GUI settings. Back up existing files before replacing them.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. **Editing Contact Tags Overwrites Existing Tags**: In `editcontact`, specifying `t/TAG` replaces all existing tags on the contact rather than appending new tags. For example, if a contact has tags `friends` and `owesMoney`, running `editcontact 1 t/source` removes both existing tags and leaves only `source`. If you wish to preserve existing tags when adding a new one, you must re-enter all existing tags alongside the new one (e.g. `editcontact 1 t/friends t/owesMoney t/source`). We plan to provide an explicit distinction between adding and replacing tags in a future update.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action                            | Format, Examples                                                                                                                                                                    |
| --------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Add**                           | `addcontact n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `addcontact n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague` |
| **Add follow-up**                 | `addfollowup DESCRIPTION ci/CONTACT_INDEX`<br> e.g., `addfollowup Interview prep ci/1`                                                                                              |
| **Add story**                     | `addstory STORY_NAME`<br> e.g., `addstory Housing Investigation`                                                                                                                    |
| **Assign**                        | `assign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `assign ci/1 si/2`                                                                                                               |
| **Clear all entries**             | `clear`                                                                                                                                                                             |
| **Delete**                        | `deletecontact INDEX`<br> e.g., `deletecontact 3`                                                                                                                                   |
| **Delete follow-up**              | `deletefollowup INDEX`<br> e.g., `deletefollowup 1`                                                                                                                                 |
| **Delete story**                  | `deletestory INDEX`<br> e.g., `deletestory 2`                                                                                                                                       |
| **Edit**                          | `editcontact INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `editcontact 2 n/James Lee e/jameslee@example.com`                                          |
| **Edit follow-up**                | `editfollowup INDEX [d/DESCRIPTION] [ci/CONTACT_INDEX]`<br> e.g., `editfollowup 1 d/Call tomorrow ci/2`                                                                             |
| **Find**                          | `findcontact KEYWORD [MORE_KEYWORDS]`<br> e.g., `findcontact James Jake`                                                                                                            |
| **Find follow-ups**               | `findfollowups KEYWORD [MORE_KEYWORDS]...`<br> e.g., `findfollowups interview meeting`                                                                                              |
| **Find stories**                  | `findstories STORY_NAME [MORE_KEYWORDS]...`<br> e.g., `findstories Housing Investigation`                                                                                           |
| **List all contacts and stories** | `list` (clears both filters)                                                                                                                                                        |
| **List contacts**                 | `listcontacts [si/STORY_INDEX]`<br> e.g., `listcontacts` (all contacts only) or `listcontacts si/1` (contacts assigned to displayed story 1)                                        |
| **List follow-ups**               | `listfollowups [ci/CONTACT_INDEX]`<br> e.g., `listfollowups` (all follow-ups) or `listfollowups ci/1` (follow-ups for contact 1)                   |
| **List stories**                  | `liststories [ci/CONTACT_INDEX]`<br> e.g., `liststories` (all stories only) or `liststories ci/1` (stories assigned to displayed contact 1)                                         |
| **Manage shortcuts**              | `shortcut [s/SHORTCUT c/COMMAND_WORD]`<br> e.g., `shortcut` or `shortcut s/ls c/list`                                                                                               |
| **Remove shortcut**               | `unshortcut SHORTCUT`<br> e.g., `unshortcut ls`                                                                                                                                     |
| **Unassign**                      | `unassign ci/CONTACT_INDEX si/STORY_INDEX`<br> e.g., `unassign ci/1 si/2`                                                                                                           |
| **Help**                          | `help`                                                                                                                                                                              |
| **Exit**                          | `exit`                                                                                                                                                                              |
