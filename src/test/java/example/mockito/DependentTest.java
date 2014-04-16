package example.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import example.mockito.Dependency;
import example.mockito.Dependent;

public class DependentTest {

    @Test
    public void test_withCompleteIsolation() {
        // Create a mock Dependency object and customize its behavior
        Dependency mockedDependency = mock(Dependency.class);
        doThrow(new RuntimeException()).when(mockedDependency).operationX();
        when(mockedDependency.operationY()).thenReturn("myString");

        // Create a 'Dependent' object by injecting mocked dependency
        Dependent classUnderTest = new Dependent(mockedDependency);

        // Test whether method1() bubbles up exception
        try {
            classUnderTest.method1();
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        // Test whether method2() calculates right string length
        int expected = 8;
        int actual = classUnderTest.method2();
        assertEquals(expected, actual);
    }

    @Test
    public void test_withRealDependency() {
        // Create a real Dependency object and turn it into a spy
        Dependency dependency = new Dependency();
        Dependency spy = spy(dependency);

        // Create a 'Dependent' object by injecting 'spied' dependency
        Dependent classUnderTest = new Dependent(spy);

        // Test if method1() invoked Dependency.operationX()
        classUnderTest.method1();
        verify(spy).operationX();
    }

    @Test
    public void test_withPartialIsolation() {
        // Create a real Dependency object, turn it into a
        // spy, then customize some behavior
        Dependency dependency = new Dependency();
        Dependency partialMock = spy(dependency);
        doThrow(new RuntimeException()).when(partialMock).operationX();

        // Create a 'Dependent' object by injecting 'spied' dependency
        Dependent classUnderTest = new Dependent(partialMock);

        // Test whether method1() bubbles up exception
        try {
            classUnderTest.method1();
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        // Test whether method2() calculates right string length returned
        // from real Dependency
        int expected = 18;
        int actual = classUnderTest.method2();

        assertEquals(expected, actual);
    }
}