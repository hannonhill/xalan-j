### Hannon Hill Fork of Xalan-J
Main development branch: `xalan-j_2_7_1_hh`

This branch contains the base 2.7.1 version of the Xalan-J project plus customizations that we have made.  It was originally created from the `xalan-j_2_7_x` branch.

### Customizations We Have Made
- Ability to enable/disable Xalan Javascript and Xalan Java extensions via a system property.  This customization exists for security reasons.
- Gutted code that cached XMLReader objects in memory (XMLReaderManager.java) as it was leaking memory and would eventually cause OutOfMemory errors.
- 
- Applied a patch from [XALANJ-2419](https://issues.apache.org/jira/browse/XALANJ-2419) to address an issue where Unicode characters composed of a surrogate pair were being treated as separate Unicode characters and serialized incorrectly by the serializer package.

#### To Build JARs:
```
ant jar
```

JAR files will be placed in `build`.  You will likely want to use both xalan.jar and serializer.jar unless you know nothing changed in one or the other.

### Links
- [Old HH Xalan Documentation](https://hannonhill.jira.com/wiki/spaces/CSCD/pages/8651846/Upgrading+XalanJ)