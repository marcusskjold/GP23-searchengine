# Supervisor meeting Notes


# 24 November (First refactor)

* We do not have to deal with Rohit
* We need to pick up pace and make some conrecte plans
* Our classes appear to be alright
* page list is abstracted a bit too much we do not add any functionality and create further documentation
* Do we like this code? too much indentation, avoid too much nesting. 
* Task concerning ranking of queries is probably the most difficult task to complete. 
* Inverted index is cool and fun. No special data structures, use Hash maps. What does the keys point towards? It must point to a word found in a list of pages. The challenge lies in balancing it with the classes. 
* In java we can de-allocate (garbecollecting). A object without any references is made into garbage. 
* Be aware of using unecessary storage (make local variables)

# Improvements until next time 

* Be less grundig, because we will have to change it later on. 
* Create a seperate file to test for each classes. 
* Create a few unit test for our other classes. Be quick and dirty, make a sanity check. Check for correct input and output. All public methods must be tested with unit tests, we can't check the private methods and therefore we could make them protected whilst testing. Assertions can be used too. 


#  1 December (Second Supervisor meeeting)
* Inverted Index feedback: it is cool to use streams, but we do not need to go through all lines first. 
* To find something in a map goes fast, it should go fast. And it does. The idea is you sacrifice start up time to get a fast search. 
* git merge--abort it doesnt undo your merge. git restore. Git stash pop ? 
* Task 5: any tips? 
* Writing report: any tips? Where should emphasis be? Emphasis should be on what we have done, and not so much on the terminology in general. Justify why we have done something and relate to terminology, but emphasis is on what we do. Give examples on how we solved something and followed a certain principle. 

* Refactoring section: It is too concrete. Write about our approach. We can add more sections, but definitely not required. When we talk about queries we can talk about how we structured our classes. 
*  We changed our database class: PH It looks good! Instead of making seperate test, make a system. 
*  Parametirized test: 
* Arguments class? Need to be an iterable collection. Make a method which ...arguments.of(int, int) could be input output. Should be done if you find it exciting. It will be good later on!
* ITU want us to stay on 2 pages. Try to adhere to this. 

# Improvements 
* 
* 
* 
* 

# 8 December (Last supervisor meeting)

* PageRanker worked to slow, but we have since fixed it: PH: it seems like you have fixed it? You have changed you API before you have to hand in. Less abstraction may be an advantage. 
* When we make our dummy tests for pages....; Just correct our tests accordingly and get it done with. If the build takes 20 sec it is perfect. 
* getIDF may be the reason for our slow querys and for future project we could consider doing it different. What about nonwords? we reduce with -1 and tf becomes 0. PH: We could also just return 0. It is a convention that we return -1 for errors. 
* rankPage. Due to our loose coupling this works pretty good. No issues.
* .merge() similar to reduce in streams. 1 is the initial value afterwards it is summarised. 
* Overall feedback: Our code looks pretty good. 
* In regards to private methods we should write about our annoyance. What is a requirement is that we should still document a class even if all methods are private. Everything that is private just needs documentation somewhere else. Document the class as a whole (What is its responsibilities, what does it do, documentation vs implementation details). What arguments are given to a method and what is returned + ensure that it works. Know your audience in regards to documentation. 
* UnitTest indirectly. Alternatively make them protected and write about it in the report. Makes you able to pin-point errors.
* Could we make the database static? PH: Sure, however what if it was an backend for many different search engines? In our case we can argument for using static methods. It is a design choice. 
* 