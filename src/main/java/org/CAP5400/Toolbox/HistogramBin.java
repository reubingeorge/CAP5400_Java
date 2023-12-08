package org.CAP5400.Toolbox;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.CAP5400.RegionOfInterest.ROI;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.*;

import static org.CAP5400.Image.Image.MAX_RGB;
@Deprecated
public class HistogramBin implements AutoCloseable{
    private List<Map<Integer, Set<Point>>> data;
    public static final String jsonFile = "bin.json";

    public HistogramBin(@NotNull  ROI region) throws Exception {
        var jsonPath = Paths.get(jsonFile);
        var objectMapper = new ObjectMapper();

        if(Files.exists(jsonPath) && Files.size(jsonPath) > 0){
            var jsonData = Files.readString(jsonPath);
            TypeReference<List<Map<Integer, Set<Point>>>> typeReference = new TypeReference<>() {};
            this.data = objectMapper.readValue(jsonData, typeReference);
        }
        else{
            computeBin(region, objectMapper, jsonPath);
        }
        region.getRegionImage().addObserver(o ->{
            try {
                computeBin(region, objectMapper, jsonPath);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public List<Map<Integer, Set<Point>>> getBin(){ return this.data; }

    private void computeBin(ROI region, ObjectMapper objectMapper, Path jsonPath) throws Exception {
        this.data = new ArrayList<>();
        for(var i = 0; i < region.getChannels(); i++){
            data.add(new HashMap<>());
            for(var j = 0; j <= MAX_RGB; j++){
                this.data.get(i).put(j, new HashSet<>());
            }
        }
        for(int i = 0; i < region.getTotalX(); i++){
            for(int j = 0; j < region.getTotalY(); j++){
                for(int k = 0; k < region.getChannels(); k++){
                    var intensity = region.getRegionImage().getPixel(i, j, k);
                    var point = new Point(i, j);
                    this.data.get(k).get(intensity).add(point);
                }
            }
        }

        var jsonData = objectMapper.writeValueAsString(data);

        Files.write(jsonPath, Collections.singleton(jsonData), StandardOpenOption.CREATE);
    }

    @Override
    public void close() {
        for (var channelBin : data) {
            for (var points : channelBin.values()) {
                points.clear();
            }
            channelBin.clear();
        }
        data.clear();
    }
}
