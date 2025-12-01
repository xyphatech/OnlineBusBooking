package com.xypha.onlineBus.buses.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xypha.onlineBus.buses.Dto.BusRequest;
import com.xypha.onlineBus.buses.Dto.BusResponse;
import com.xypha.onlineBus.buses.Service.BusServiceImpl;
import com.xypha.onlineBus.multipart.MultipartInputStreamFileResource;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bus")
public class BusController {


    private final BusServiceImpl busService;

    private final ObjectMapper objectMapper;

    public BusController(BusServiceImpl busService, ObjectMapper objectMapper) {
        this.busService = busService;
        this.objectMapper = objectMapper;
    }

    // Pagination (MyBatis version)
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getBusPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        List<BusResponse> buses = busService.getBusPaginated(page, size);
        long totalItems = busService.countBuses();

        Map<String, Object> response = new HashMap<>();
        response.put("buses", buses);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalItems", totalItems);
        response.put("totalPages", (int) Math.ceil((double) totalItems / size));

        return ResponseEntity.ok(response);
    }

    // Add Bus
    @PostMapping
    public ResponseEntity<BusResponse> addBus(@Valid @RequestBody BusRequest busRequest) {
        BusResponse saved = busService.addBus(busRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all buses
    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBus() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // Get one bus
    @GetMapping("/{id}")
    public ResponseEntity<BusResponse> getBus(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    // Update bus
    @PutMapping("/{id}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusRequest request
    ) {
        return ResponseEntity.ok(busService.updateBus(id, request));
    }

    // Delete bus
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.ok("Bus deleted successfully");
    }

    // Upload image + create Bus
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusResponse> uploadBus(
            @RequestPart(value = "busRequestString", required = true) String busRequestString,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {

//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
        BusRequest busRequest = objectMapper.readValue(busRequestString, BusRequest.class);
        if (!file.getContentType().toLowerCase().startsWith("image")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
        }

        //////////////////////////////////////////////saving in local
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        Path uploadPath = Paths.get("C:\\Users\\Leon\\Desktop\\uploads");
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//        Path filePath = uploadPath.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


///     /////////////////////////////Store with String type////////////////////////////////////
//        byte[] bytes = file.getBytes();
//        String base64Image = Base64.getEncoder().encodeToString(bytes);
//        busRequest.setImgUrl(base64Image);




        ////////////////////////////////  Store in cloud ////////////////////////////////
        ////saving in cloud imagekit
        String privateKey= "private_ju7N56tZcDf1kY6Ws8QnQTd/bvg=";
        String uploadUrl = "https://upload.imagekit.io/api/v1/files/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth( privateKey, "");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getOriginalFilename(), file.getInputStream()));
        body.add("fileName", file.getOriginalFilename());
        body.add("folder","/bus-images");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        //Parse Json To get URL
        ObjectMapper mapper = new ObjectMapper();
        String imgUrl = mapper.readTree(response.getBody()).get("url").asText();

        busRequest.setImgUrl(imgUrl);
        BusResponse savedBus = busService.addBus(busRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBus);
    }
}
