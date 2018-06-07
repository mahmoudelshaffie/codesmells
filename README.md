# codesmells
- This is an illustrative example of a bad and good HttpInterceptor implmented to handle http requests among microservices.
- I had refactored it, to meet clean code standards
- The bad implementation is named as BadHttpInterceptor and located in org.codesmells.bad
- The good implementation is named as GoodHttpInterceptor and located in org.codesmells.good

# BadHttpInterceptor:
- According to the name of the unit it should acts as an interceptor for Http requests and response, just intercepting certain cutting points such as before sending a request or receiveing an error response.
- The major problem addressed here is the voilation of single repsonisbility, actually it has intercepting responsiblity, and many handling ones.
- Self calls, in handling scenarios, adding a complexity to the logical flow, accross several cases. un-clear flow of logic.
- It handles also a global state. (relogin maximum trials).
- The violation of single reponsibility, reduces another technical debt issue while writing unit tests for this unit.
	- Every test should be affected only to the change related to the part under testing. In other words, test cases for interception scenarios shouldn't be affected due to a change in a handling scenario. It may be affected by the result of the handling scenarion but not the implementation detail.
	- Un-breaking the logic, and self calls, increase the complexity & redundancy of buliding test cases.
- The violation of single reponsibility also reduce another violation, the open/closed. Handling cases are not closed for modifications. BadHttpInterceptor.onResponse() method.
- Also violation of single responsibility, correlating massive depedencies also. Actually not reflected on the code, because I write it based on my memory. This also increase complexity of writing test cases.


# GoodHttpInterceptor:
- Acheiving single reponsiblity by extracting handling logic outside the interceptor. It only intercepts the requests and forward them to handlers.
- Usage of chaing or responibility pattern in writing Http Request/Response Handlers, achieve open/closed principle.
	- HttpRequestHandlersChain class, which serve as a chain of request handlers, which should be run every HttpRequest.
		- Rather than onRequest method on BadHttpInterceptor, this approach allows adding new handlers, through register method.
		- So if there  is any handling requirement will be added, there is no already units will be touched, and their test cases also. (technical debt is managed here)
	- HttpErrorReponseFilter & HttpErrorReponseChain also follow the same approach to acheive the OCP for handling error responses.
		- Rather than onResponseError, which has many if-else "branches".
		- Actually nested if-else, works as same big O complexity of algorithms, nested iterations, increases time of algorithms. Also nested else-if increases complexity of unit testing. n to the power n test cases.
	- Breaking down the handling logic into smaller units, make it easier to be maintained, and tested.
	- Moving logic of relogin and global state to HttpReloginHandler increaes cohesion of units. So it increase maintainability.
