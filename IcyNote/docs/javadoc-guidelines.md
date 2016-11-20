#Javadoc guidelines summary

This document is a quick summary of the guidelines to write effective javadoc comment. 
It is compiled from http://www.oracle.com/technetwork/articles/java/index-137868.html and 
does not pretend to replace it. Please refer to this official link for further details and explanations.


## General format of a javadoc comment 
```
/**
 * The first sentence of a javadoc comment is a dense summary that ends with a dot.
 * The first sentence is very important because it is copied into the summary field in the javadoc.
 *
 * <p>
 * pre condition: you want to write good comments. <br>
 * post condition: the written comment is indeed good. <br>
 * error case: when you have a bike accident, for instance. <br>
 * 
 * @author Your Name (required for classes and interfaces)
 * @version version-number (required for classes and interfaces)
 * @see package_name.class_name#member_name (optional)
 * @param param_name param_description_and_consequences (required for methods)/
 * @throws ExceptionType if blahblah 
 * @return description_of_return_value (required for methods)
 */
 ```
 
## The first sentence

The first sentence of a javadoc comment is a dense summary that ends with a dot. 
It is very important because it is copied into the summary field in the javadoc.

## Formating 

You can use html to format a javadoc comment. But beware ! The comment should be humanly readable,
so you might want to keep it simple : `<p>` to separate paragraphs, `<br>` to insert a newline.

## Document API

A javadoc comment is not a user manual ! You should describe the API and the expected behaviour **as well as the error cases** but 
not give examples or explanatory metaphors which are reserved for user manuals.

## Recommended practice 

Optional but strongly recommended rules to writing robust javadoc comment (not in the link above):
- document pre condition(s) of a method
- document post condition(s) of a method
- document error cases and corresponding behaviour
- always state all the possible return values of a method (including whether it can be null or not)

## Tags

### For a class or interface

- `@author Your Name` required by standard, usefull to know who to ask for further explaination
- `@version XX` required by standard
- `@see package.class#member` optional, creates a link to the specified class or member (method)

### For a class or interface

- `@author Your Name` required by standard, usefull to know who to ask for further explaination
- `@version XX` required by standard
- `@see package.class#member` optional, creates a link to the specified class or member (method)


### For a method

- include a line stating pre conditions (strongly recommended)
- include a line stating post conditions (strongly recommended)
- include a line stating behaviour in case of error (strongly recommended)
- `@param param_name description` null of not ? when ? 
- `@return description_of_return_value` null or not ? when ?
- `@throws ExceptionType if blahblah`
- `@see package.class#member` optional, creates a link to the specified class or member (method)
 
### More info 

- javadoc official guidelines: http://www.oracle.com/technetwork/articles/java/index-137868.html
- the `@see` tag: http://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html#see
