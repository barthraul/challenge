package bold.challenge.Tests;

import java.util.Arrays;
import org.junit.Test;
import bold.challenge.MainProcess;
import bold.challenge.ParsingThread;
import junit.framework.TestCase;

public class GeneralTest extends TestCase {
	@SuppressWarnings("static-access")
	@Test
    public void testClass() {          
		MainProcess mainProcessTester = new MainProcess();
		ParsingThread threadTester = new ParsingThread("Test");
        
		// Test building file room name
		assertEquals("Must be -roomA", "-roomA", mainProcessTester.buildFileRoomName("roomA"));
        assertEquals("Must be -roomB", "-roomB", mainProcessTester.buildFileRoomName("roomB"));
        assertEquals("Must be -roomC", "-roomC", mainProcessTester.buildFileRoomName("roomC"));
        
        // Test room filter condition (all = all rooms considered)
        mainProcessTester.setConsideredRooms(Arrays.asList("roomA,roomB".split(",")));
        assertEquals("RoomA exists!", true, mainProcessTester.checkRoomFilterCondition("roomA"));
        assertEquals("RoomC does not exist!", false, mainProcessTester.checkRoomFilterCondition("roomC"));
        assertEquals("RoomB exists!", true, mainProcessTester.checkRoomFilterCondition("roomB"));       
        mainProcessTester.setConsideredRooms(Arrays.asList("all"));
        assertEquals("All rooms!", true, mainProcessTester.checkRoomFilterCondition("roomA"));
        
        // Test department filter condition (all = all departments considered)
        mainProcessTester.setConsideredDep(Arrays.asList("HR1,HR5,HR6".split(",")));
        assertEquals("HR1 does not exist!", false, threadTester.checkDeparmentFilterCondition(true, false, false));
        mainProcessTester.setConsideredDep(Arrays.asList("all".split(",")));
        assertEquals("All departments!", true, threadTester.checkDeparmentFilterCondition(true, false, true));
    }
}
