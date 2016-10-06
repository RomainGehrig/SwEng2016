#Coding conventions

### never use null
- never use null attribute and never test whether an attribute is null inside the application.
- some test can be performed at the boundary between the app and the plugins (UI, Storage, etc.)
- for possibly empty element, use java's `Optional<T>`
- this convention is more and more used since java 8

### getters
use the following format : `getAttr()` instead of `attr()`
