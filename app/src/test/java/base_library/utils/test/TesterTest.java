package base_library.utils.test;

import com.futurist_labs.android.base_library.utils.test.*;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Galeen on 2019-08-13.
 */
public class TesterTest {
    @Test
    public void testNonNullsObject() {
        TestObjectNulls testObject = new TestObjectNulls(0, "", 0L, false);
        String nullFields = Tester.getNullFields(testObject, true);
        assertNull("has null fields", nullFields);
    }

    @Test
    public void testNullsObject() {
        TestObjectNulls testObject = new TestObjectNulls(0, null, null, false);
        String nullFields = Tester.getNullFields(testObject, true);
        assertNotNull("must have errors", nullFields);
        System.out.println("testNullsObject : " + nullFields);
    }

    @Test
    public void testPrimitivesObject() {
        TestObjectPrimitives testObject = new TestObjectPrimitives(0, "", 1L, false);
        String nullFields = Tester.getNullFields(testObject, true);
        assertNotNull("must have errors", nullFields);
        System.out.println("testPrimitivesObject : " + nullFields);
    }

    @Test
    public void testExcludeObject() {
        TestObjectExcludes testObject = new TestObjectExcludes(0, "", null, false);
        String nullFields = Tester.getNullFields(testObject, true);
        assertNull("time is not excluded from check", nullFields);
    }

    @Test
    public void testMandatoryObject() {
        TestObjectMandatory testObject = new TestObjectMandatory(0, "1", null, false);
        String nullFields = Tester.getNullFields(testObject, false);
        assertNull("time is check when is not mandatory", nullFields);
    }

    @Test
    public void testMandatoryEmptyObject() {
        TestObjectMandatory testObject = new TestObjectMandatory(0, "", null, false);
        String nullFields = Tester.getNullFields(testObject, false);
        assertNotNull("id is empty and mandatory", nullFields);
        System.out.println("testMandatoryEmptyObject : " + nullFields);
    }
}