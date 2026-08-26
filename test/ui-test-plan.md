# Alexa console UI test plan

## How to run this plan

Compile the Java sources with Java 25, then run `src.main.java.Alexa`. Each Input block below is sent as standard input to a fresh program session. Compare the captured standard output exactly with the corresponding Expected output block, ignoring only line-ending style.

## Test case: Greeting and farewell

**Aim:** Verify that Alexa starts with its greeting and ends cleanly after `bye`.

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
                 A L E X A
Hello! I'm Alexa.
What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add and list all task types

**Aim:** Verify that `Todo`, `Deadline`, and `Event` are stored as `Task` objects and displayed with their type-specific details.

**Input:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
____________________________________________________________
                 A L E X A
Hello! I'm Alexa.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
