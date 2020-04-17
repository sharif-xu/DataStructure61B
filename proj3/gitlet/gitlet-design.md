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


## Persistence

