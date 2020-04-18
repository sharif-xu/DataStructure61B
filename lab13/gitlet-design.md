# Gitlet Design Document
**Name**: Ruize Xu

## **Classes and Data Structures**

**Command**
This class stores all the commands input from the user and then deal with them.
**Fields**

1. String _command: Stores the command type.
2. String _branch: Show that which branch now is working on.

**Main**
This class is the entry to the entire project which throws exceptions and calls other functions.
**Commit**
This class stores corresponding data structure about a commit class.
**Fields**

1. String _id: Stores the id of one commit.
2. String _message: Stores the commit message.
3. String _date: Stores the commit time in java Date class.
4. LinkedList _parents: Stores the parents of one commit.
5. HashMap _blobs: Stores a hash map which maps the file to the commit.
## **Algorithms**

**Command Class**

1. Command(String... args): The class constructor. Initialize the command for this run and saves the instant variables for implementing corresponding function.
2. init(String args): Initialize a new gitlet by creating a .gitlet directory, and subdirectories as .staging and .bombs 
3. add(String... args): First decide whether it is a failure case. Then add the modified files into the staging area, which is a individual directory in this project.
4. commit(String... args): Save current files so they can be fetch later. To be specefic, once committed the files, the files in staging area should be cleared for no exception.
5. rm(String.. args): Decided on the file, where staged files get unstaged and tracked staged to be removed.
6. log: Get corresponding sha-1 id, committing time, optional parent ids and committing messages and show in format.
7. global-log: Same with log, but it can directly outputs all the stored information ever saved.
8. find: According to the message, search for the ids meets the message.
9. status: Find files of different states and print them out. Note that the format of output status should be exactly matched with the standard.

**Main Class**

1. dealCmd(String... args): Decide what command has been input and then call corresponding function in Command.java.

**Commit Class**

1. Commit(): Initialize all the variables in this class. Set the message as the given "initial commit" and date as standard java initial time with other empty initial value.
2. Commit(String msg, Commit commit): Make a new commit with some of the information from last commit, and add some new messages about this new commit.
## **Persistence**

In order to correctly use this Gitlet, correct input should be met. The correct usage of this program always should be: 
java gitlet/Main \[command\] [operand] 
To correctly do this,

1. Ensure there is a .gitlet directory in the working directory, if you want to call commands except **init**.
2. Input correct number of operands for relating commands. e.g. If you call **java gitlet.Main status**, you should not input any more operands otherwise it will cause an error.
3. Make sure that you follow the rules of gitlet instead of a real git such that some minor difference won't throw an exception.