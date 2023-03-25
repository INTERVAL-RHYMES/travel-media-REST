package com.example.travelmediarest.config;

public class MockMockitoMockBean {
    /*

At the end its easy to explain. If you just look into the javadocs of the annotations you will see the differents:

#####
@Mock: (org.mockito.Mock)

Mark a field as a mock.

Allows shorthand mock creation.
Minimizes repetitive mock creation code.
Makes the test class more readable.
Makes the verification error easier to read because the field name is used to identify the mock.

####
@MockBean: (org.springframework.boot.test.mock.mockito.MockBean)

Annotation that can be used to add mocks to a Spring ApplicationContext. Can be used as a class level annotation or on fields in either @Configuration classes, or test classes that are @RunWith the SpringRunner.

Mocks can be registered by type or by bean name. Any existing single bean of the same type defined in the context will be replaced by the mock, if no existing bean is defined a new one will be added.

When @MockBean is used on a field, as well as being registered in the application context, the mock will also be injected into the field.


####
Mockito.mock()

Its just the representation of a @Mock.


https://stackoverflow.com/questions/44200720/difference-between-mock-mockbean-and-mockito-mock
     */
}
