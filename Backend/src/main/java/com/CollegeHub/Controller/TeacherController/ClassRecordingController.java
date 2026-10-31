package com.CollegeHub.Controller.TeacherController;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher/recordings")
public class ClassRecordingController {

//    private final ClassRecordingService service;
//    private final ClassRecordingRepository classRecordingRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public ClassRecordingController(ClassRecordingService service, ClassRecordingRepository classRecordingRepository){
//        this.service = service;
//        this.classRecordingRepository = classRecordingRepository;
//    }
//
//
//    //upload the class recordings for teahcer
//    @PostMapping(
//            value = "/upload",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
//    )
//    public ResponseEntity<?> uploadClassRecording(
//            @RequestPart("video")MultipartFile video,
//            @RequestPart("data")ClassRecordingRequestDTO request
//
//            ) throws IOException{
//
//        if(video.isEmpty()){
//            return ResponseEntity.badRequest().body("Video file is missing...???");
//        }
//        Authentication auth = SecurityContextHolder
//                .getContext().getAuthentication();
//
//        String email = auth.getName();
//
//        Users user = userRepository.findByEmail(email).orElseThrow();
//
//
//        ClassRecording saved = service.upload(video,request , user);
//
//        System.out.println(user.getEmail());
//
//        Map<String, Object> response = Map.of(
//                "id", saved.getId(),
//                "title", saved.getTitle(),
//                "classDate", saved.getClassDate(),
//                "periodNumber", saved.getPeriodNumber(),
//                "branch", saved.getBranch(),
//                "year", saved.getYear(),
//                "videoPath", saved.getVideoPath(),
//                "message", "Upload successful"
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    //get the recordings for the current teacher
//    @GetMapping
//    public List<ClassRecording> fetchRecordings(
//            @RequestParam(required = false)LocalDate date,
//            @RequestParam(required = false) String subject,
//            @RequestParam(required = false) Integer period
//            ){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        String email = auth.getName();
//        Users teacher = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Teacher not found...???"));
//
//        return null;
//    }
//
//    @GetMapping("/{id}/video")
//    public ResponseEntity<Resource> getRecordingVideo(@PathVariable Long id) {
//
//        Resource video = service.getVideoByRecordingId(id);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
//                .body(video);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteRecording(@PathVariable Long id) {
//
//        // 1. Get recording
//        ClassRecording recording = classRecordingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Recording not found"));
//
//        // 2. Delete video file
//        if (recording.getVideoPath() != null) {
//            try {
//                Path path = Paths.get(recording.getVideoPath());
//                Files.deleteIfExists(path);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to delete video file");
//            }
//        }
//
//        // 3. Delete DB record
//        classRecordingRepository.delete(recording);
//
//        return ResponseEntity.ok("Recording deleted successfully");
//    }



}
