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

### License
```
Copyright 2004 The Apache Software Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```