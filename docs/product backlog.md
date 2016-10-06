# Product Backlog

- uses cases imply changes in the core application
- UI features imply changes in the UI part

## IcyNote Use Cases in short

**basic use cases**
- listing notes : notes' title are returned to user as a list
- sorting the list of notes by date of creation
- sorting the list of notes by date of last modification
- displaying a note : the note's title and content are returned to user
- editing a note : user modifies the note's title or content and the system persists the changes
- deleting a note

**tag-related use cases**
- adding tags to a note
- removing a tag from a note
- listing all existing tags
- listing all existing tags starting with a given prefix
- listing notes associated with every tag in a given set of tags
- listing tags associated with a given note

**storage-related use case**
- exporting a note to an external storage system with a given format
     - the storage system is Google Drive
     - the storage system is Dropbox
     - the format is HTML
     - the format is PDF
- saving a note history in a content versioning system


## IcyNote's UI features

**basic ui features**
- a clickable menu displays a list of the notes' title (when clicked, the note is displayed)
     - sorted by date of creation
     - sorted by date of last modification
     - sorted by alphabetical order
- a note's title and content can be displayed in an editable manner
- a note can be deleted

**tag and meta-data related ui features**
- a note's list of tag can be displayed and edited
- a note's meta-data (date, possibly author) can be displayed
- a clickable menu displays a list of every tag 
     - when clicked, notes associated with the given tag are listed
- a clickable menu displays a list of every tag starting with a given prefix
     - when clicked, notes associated with the given tag are listed
- notes associated with every tag in a given set of tag can be listed in a clickable list

**advanced ui features**
- media files can be inserted and displayed inside a note
     - the media file is a picture
     - the media file is an audio file
     - the media file is a geoposition
- user can record a media file without having to live the UI
     - takes a picture with the camera
     - records an audio memo using the microphone
     - hand writes on a blank picture
- formatted using a markup language are displayed with the appropriate format
     - using markdown as the markup language
     - using BBCode as the markup language
- syntax coloration is available for embedded code snippets in a given language
     - for Java programming language
     - for C++ programming language
- latex formula are displayed as images and can be edited when the image is clicked
- pictures taken with the app can be cropped and rotated with a system similar to Office Lens

## Other features

- DB implementation
