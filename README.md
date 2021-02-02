# realmToCSV
An Android app that transforms a realm DB to a set of csv files.

# Setup
Before you use the App you have to setup a few things:
 - get realm studio from here https://docs.realm.io/sync/realm-studio, open your realm DB, and choose File > Save model definitions > Java. Put the savec classes in the package 'models' of the project,
 - for each exported class:
   - make it 'implements RealmObjectExportable' (NOTE: by default, each exported class should extend the RealmObject class, if not add it manually), 
   - define the interface methods:
    - getSchema() should return the schema of the class as a header for the CSV file, for instance 'return "field1,field2,field3";'
    - toRow() should return a CSV row consisting of the fields of the object, it should follow the structure of the header. For For Realm lists, put the field into the method Tools.parseRealmList(...), for instance suppose field2 is a list, toRow() would look like 'return field1+","+Tools.parseRealmList(field2)+","+field3;',
    - getId() should return something that is unique for the object, so that when another object refer to that object it would use that unique id. For instance, the id, the first+lastName, email, phoneNum,....,
    - implement toString() and make it return the getId value simply 'return getId();'.
   - put your realm DB file into the 'raw' folder in the resources folder, name it 'default0.realm'.
   
 At this point, you are ready to compile and run the app. Press the 'Transform' button and the process of conversiobn starts. You will find the output in the '/data/data/org.islamcontrib.realmtocsv/' with the 'realmTransformed_' prefix. You can use a root file explorer to get the files or a 'adb pull' command to pull the files from the phone using adb.
 
 Feel free to edit/improve the project (FYI: The usage of GSON can make it more simpler, but realm uses a proxy class when compiling the project which makes the field unaccessible for GSON, I didn't spend too much time on thise point).
