# Project requirements
This document describes (to the best of my ability and the ability of the team) the requirements of the UTLookup project. 

### Costs 
We don't have any cost estimates right now. The reason for this is that we don't know exactly what we would be paying for. Maybe testing would incur a cost, but it is not clear how much that would cost. Development of UTLookup is being undertaken in the free time of its developers as the original intention is for it to be open source (and free).

### Time requirements
A project of this kind would usually take about two weeks of full time to get the functionality and a very basic UI in play.

Considering that we chose Android as our platform of initial implementation, we could add a week to that development time simply because Android has so many devices and versions that require support, and it wouldn't really be logical to underestimate the amount of time required. 

That is the amount of time required for a prototype. For the full product, it could take much longer simply because there is more testing and bug fixing and tweaking that can only occur after many hours invested in testing the product on many different devices.

### User Experience (UX) and functionality requirements
UTLookup should make it very easy (maybe even trivial) for the user to do the following things:
##### Minimum Requirements
1. Search for courses by any criterion: what this means is that the user can use the application to search for courses by what prerequisites it requires, what corequisites it requires, what exclusions it has, course code, course name, keywords in the description, and so on and so forth in terms of metadata.
2. Search for timetable slots: the user has some timeslot in their schedule that is void of any classes, and they would like to query by this timeslot (say monday 11-1), and this query would return all the courses in that timeslot, which the user can then pick from.

##### Extra requirements 
3. Give the user the option to craft their timetable. They will pick courses from their queries and design their own timetable. They can design multiple timetables to see what is best for them.
4. (Depends on 3): give the user the option to notify them when classes are about to start. 
5. (Depends on 3): show the user their timetable at any time. Add the ability to share the timetable on any social platform (facebook, twitter, instagram, and so on).
6. Be able to inform the user which courses they may take (i.e given the prerequisites, corequisites, program requirements, and so on). This would require UTLookup to somehow gather information about the user, which is partly a design and partly an implementation detail. Here are two options:
   1. UTLookup gathers the data about subject posts in U of T and see what the students are required to take. This will enable the ability to prompt the user about their subject post and allow the user to check which courses they have taken. 
   2. UTLookup tells the user to input all courses the user has taken. This is not the preferred approach as it doesn't scale well when the user has many courses.

### Future endeavours
This is not strictly part of the requirements of UTLookup per se, but it would be valuable to keep this in mind when we are implementing this spec. 

Adam had the idea of defining an XML schema that can be implemented by all universities and simply "inject" that implementation into our app, so that the user can use UTLookup for their own university. 

While XML may seem like a strict choice we can define an equivalent JSON schema since some universities (such as Waterloo) export their data in their API as JSON rather than XML, simply because it is more common for web apps to use JSON rather than XML, even though JavaScript has native support for both.

A large extension of UTLookup would basically be transforming UTLookup into a university companion: professors that teach classes can use the UTLookup platform to set deadlines for assignments, exams, tests, and so on, and the user of UTLookup can immediately be reminded/notified when such a thing is going to happen. 
