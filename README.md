##### Alexa 🤖

> “Small steps every day lead to big results.”

Alexa is a simple command-line task manager that helps you keep track of what needs doing. It is:

- text-based
- easy to learn
- ~~complicated~~ *simple* and **fast** to use

All you need to do is:

1. download the application from the [course project page](https://github.com/EdmundTan37/ip/releases).
2. run the JAR file.
3. enter tasks such as `todo read book`.
4. let Alexa organize them for you 😉

Features:

- [x] To-do tasks
- [x] Deadlines and events
- [x] Marking, unmarking, and deleting tasks
- [x] Keyword search using `find KEYWORD`
- [x] Automatic task saving
- [ ] Reminders — coming soon

---

If you are a Java programmer, you can also use Alexa to practise Java. Its entry point is:

```java
public static void main(String[] args) {
    new Alexa(Path.of("data", "alexa.txt")).run();
}
```
