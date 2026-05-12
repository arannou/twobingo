// EXAMPLE USAGE: NotificationCenter

// ============================================
// FRONTEND EXAMPLES
// ============================================

// 1. Initialize in App.vue
import { useNotificationStore } from '@/stores/notifications';
import { useUserStore } from '@/stores/user';

export default {
  setup() {
    const notificationStore = useNotificationStore();
    const userStore = useUserStore();

    onMounted(async () => {
      try {
        // Get current user ID
        const userId = userStore.user?.id;
        
        // Initialize notification center
        await notificationStore.initializeNotificationCenter(userId);
        console.log('Notification center initialized');
      } catch (error) {
        console.error('Failed to initialize notification center:', error);
      }
    });

    onUnmounted(() => {
      notificationStore.disconnect();
    });

    return { notificationStore };
  }
};


// 2. Listen to notifications in a component
import { useNotificationStore } from '@/stores/notifications';

export default {
  setup() {
    const notificationStore = useNotificationStore();

    onMounted(() => {
      // Listen for grid updates
      notificationStore.onEvent('grid:updated', (gridData) => {
        console.log('Grid was updated:', gridData);
        // Refresh local grid data
      });

      // Listen for cell changes
      notificationStore.onEvent('cell:changed', (cellData) => {
        console.log('Cell changed:', cellData);
        // Update cell in UI
      });

      // Listen for user events
      notificationStore.onEvent('user:joined', (userData) => {
        console.log('User joined:', userData);
      });
    });

    return { notificationStore };
  }
};


// 3. Programmatically add notifications
import { useNotificationStore } from '@/stores/notifications';

const notificationStore = useNotificationStore();

// Success notification
notificationStore.addNotification({
  title: 'Success',
  message: 'Grid saved successfully',
  type: 'success'
});

// Error notification
notificationStore.addNotification({
  title: 'Error',
  message: 'Failed to update grid',
  type: 'error',
  autoClose: false // Don't auto-dismiss
});

// Warning notification
notificationStore.addNotification({
  title: 'Warning',
  message: 'Grid has unsaved changes',
  type: 'warning'
});

// Info notification
notificationStore.addNotification({
  title: 'Info',
  message: 'Another user is editing this grid',
  type: 'info'
});


// 4. Emit custom events
const notificationStore = useNotificationStore();

notificationStore.emitEvent('custom-event', {
  data: 'some data',
  timestamp: new Date()
});


// ============================================
// BACKEND EXAMPLES (Java)
// ============================================

// 1. Notify a specific user
@Autowired
private NotificationCenter notificationCenter;

public void notifyUserOfGridUpdate(String userId, GridDto gridDto) {
    notificationCenter.notifyUser(userId, "grid:updated", gridDto);
}


// 2. Broadcast to all users
@PostMapping("/grid/{gridId}")
public ResponseEntity<GridDto> updateGrid(@PathVariable String gridId, @RequestBody GridDto gridDto) {
    Grid updatedGrid = gridService.update(gridId, gridDto);
    
    // Notify all connected clients
    notificationCenter.broadcastNotification("grid:updated", new GridDto(updatedGrid));
    
    return ResponseEntity.ok(new GridDto(updatedGrid));
}


// 3. Notify multiple users
@PostMapping("/participation")
public ResponseEntity<ParticipationDto> addParticipation(
    @RequestBody ParticipationRequest request) {
    
    Participation participation = participationService.create(request);
    
    // Notify all participants of the grid
    List<String> participantUserIds = participationService.getUserIdsForGrid(request.getGridId());
    notificationCenter.notifyUsers(
        participantUserIds,
        "participation:joined",
        new ParticipationDto(participation)
    );
    
    return ResponseEntity.ok(new ParticipationDto(participation));
}


// 4. Notify on cell change
@PutMapping("/cell/{cellId}")
public ResponseEntity<CellDto> updateCell(@PathVariable String cellId, @RequestBody CellDto cellDto) {
    Cell updatedCell = cellService.update(cellId, cellDto);
    
    // Notify all participants of the grid
    String gridId = updatedCell.getGrid().getId();
    List<String> userIds = participationService.getUserIdsForGrid(gridId);
    
    notificationCenter.notifyUsers(userIds, "cell:changed", new CellDto(updatedCell));
    
    return ResponseEntity.ok(new CellDto(updatedCell));
}


// 5. Check user connection status
public boolean isUserOnline(String userId) {
    return notificationCenter.isUserConnected(userId);
}


// 6. Get connected users count
@GetMapping("/status")
public ResponseEntity<Map<String, Object>> getStatus() {
    Map<String, Object> status = new HashMap<>();
    status.put("connectedUsers", notificationCenter.getConnectedUsersCount());
    return ResponseEntity.ok(status);
}


// ============================================
// EVENT NAMING CONVENTIONS
// ============================================

/*
Use consistent naming for events:

- grid:updated - Grid data changed
- grid:deleted - Grid was deleted
- cell:changed - Cell value changed
- cell:marked - Cell marked/unmarked
- user:joined - User joined grid
- user:left - User left grid
- participation:added - User added to participation
- participation:removed - User removed from participation
- message:new - New message
- error:occurred - Error event

Format: resource:action
*/


// ============================================
// TESTING THE NOTIFICATION CENTER
// ============================================

// Test setup instructions:
// 1. Start backend: mvn spring-boot:run
// 2. Start frontend: npm run dev
// 3. Open browser console
// 4. Use browser's Network tab to see Socket.io messages
// 5. Send test notifications via API calls:

/*
curl -X POST http://localhost:8081/grid/test-grid \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Grid"}'

This will trigger the grid:updated notification broadcast.
*/


// ============================================
// ADVANCED: CUSTOM EVENT HANDLING
// ============================================

// Listen to connection events
const notificationStore = useNotificationStore();

notificationStore.onEvent('connect', () => {
  console.log('Connected to notification server');
});

notificationStore.onEvent('disconnect', () => {
  console.log('Disconnected from notification server');
});


// Handle errors
notificationStore.onEvent('error', (error) => {
  console.error('Notification error:', error);
  // Show error to user
  notificationStore.addNotification({
    title: 'Connection Error',
    message: 'Failed to receive notifications',
    type: 'error'
  });
});
