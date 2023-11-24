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


#  1 December 

# Improvements 