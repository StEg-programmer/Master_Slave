
package com.example.slaveserevice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/slave")
public class SlaveController {

    private final Map<Integer, String> dataStorage = new HashMap<>();
    @GetMapping("/{id}")
    public ResponseEntity<String> getData(@PathVariable int id) {
        return dataStorage.containsKey(id) ?
                ResponseEntity.ok(dataStorage.get(id)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
    }

    @PostMapping
    public ResponseEntity<String> createData(@RequestBody String data) {
        System.out.println("Received data: " + data);
        int id = dataStorage.size() + 1;
        dataStorage.put(id, data);
        return ResponseEntity.ok("Data added with ID: " + id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateData(@PathVariable int id, @RequestBody String data) {
        if (dataStorage.containsKey(id)) {
            dataStorage.put(id, data);
            return ResponseEntity.ok("Data updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteData(@PathVariable int id) {
        if (dataStorage.remove(id) != null) {
            return ResponseEntity.ok("Data deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
    }
}
