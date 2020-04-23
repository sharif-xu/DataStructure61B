# Gitlet Design Document

**Name**: Ruize Xu

## Classes and Data Structures
### Commit
The class used to store the content of each commit command,
include commit message, timestamp, parent commit and so on.
#### Instance
1. String _message: Store the input message of the commit command.
2. Date or String _timeStamp: Store the current time when the new commit
object was created. If current commit object does not have a parent commit, 
_timeStamp will be initialize as "00:00:00 UTC, Thursday, 1 January 1970".
3. HashMap\<String\> _blobs: A list of strings of hashes of Blobs that are being
tracked.
4. 

### Stage
The class used to store changes of blobs after executing command add 
before executing commit command to the branch.

#### Instance 
1. Hashmap\<String\> blobs: store blobs which have been added or modified.
2.


## Algorithms
### Commit class
1. Commit(String message, String timeStamp, Commit Parent): Constructor function 
used to create a Commit object.
2. String hasHashName(): Returns hashCode of this commit Node.


## Persistencev 
Describe your strategy for ensuring that you don’t lose the state of your program across multiple runs. Here are some tips for writing this section:

This section should be structured as a list of all the times you will need to record the state of the program or files. For each case, you must prove that your design ensures correct behavior. For example, explain how you intend to make sure that after we call java gitlet.Main add wug.txt, on the next execution of java gitlet.Main commit -m “modify wug.txt”, the correct commit will be made.
A good strategy for reasoning about persistence is to identify which pieces of data are needed across multiple calls to Gitlet. Then, prove that the data remains consistent for all future calls.
This section should also include a description of your .gitlet directory and any files or subdirectories you intend on including there.

 