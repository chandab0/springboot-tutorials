package com.example.demo;

// import com.xuggle.mediatool.IMediaReader;
// import com.xuggle.mediatool.IMediaWriter;
// import com.xuggle.mediatool.ToolFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.FileSystemResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @RestController
// @RequestMapping("/videos")
// public class VideoController {

//     @Value("${external.drive.location}")
//     private String externalDriveLocation;

//     @GetMapping("/{filename:.+}")
//     public ResponseEntity<StreamingResponseBody> streamVideo(
//             @PathVariable String filename
//     ) throws IOException {
//         Path videoPath = Paths.get(externalDriveLocation, filename);
//         Resource videoResource = new FileSystemResource(videoPath);

//         if (!videoResource.exists()) {
//             return ResponseEntity.notFound().build();
//         }

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.valueOf("video/mp4")); // Set the Content-Type header to video/mp4

//         StreamingResponseBody responseBody = outputStream -> {
//             // Create a media reader for the input MKV file
//             IMediaReader mediaReader = ToolFactory.makeReader(videoResource.getFile().getAbsolutePath());

//             // Create a media writer for the output stream
//             IMediaWriter mediaWriter = ToolFactory.makeWriter(outputStream, mediaReader);

//             // Set the container format to MKV
//             mediaWriter.setContainerFormat("matroska");

//             // Set the video stream index
//             int videoStreamIndex = 0;
//             mediaWriter.addVideoStream(videoStreamIndex, 0, IMediaWriter.Codec.ID.CODEC_ID_H264,
//                     mediaReader.getContainer().getVideoTracks().get(videoStreamIndex).getVideoWidth(),
//                     mediaReader.getContainer().getVideoTracks().get(videoStreamIndex).getVideoHeight());

//             // Set the audio stream index
//             int audioStreamIndex = 0;
//             mediaWriter.addAudioStream(audioStreamIndex, 0, IMediaWriter.Codec.ID.CODEC_ID_AAC,
//                     mediaReader.getContainer().getAudioTracks().get(audioStreamIndex).getChannels(),
//                     mediaReader.getContainer().getAudioTracks().get(audioStreamIndex).getSampleRate());

//             // Read and transcode each packet from the input media reader to the output media writer
//             while (mediaReader.readPacket() == null) {
//                 mediaWriter.flush();
//             }

//             // Close the media reader and writer
//             mediaReader.close();
//             mediaWriter.close();
//         };

//         return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
//     }
// }

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

//import javax.servlet.http.HttpServletResponse;

@RestController
public class VideoController {

    @Autowired
    private MkvService mkvService; // Inject your MKV processing service

    @GetMapping("/api/videos/{filename}")
    public StreamingResponseBody streamVideo(@PathVariable String filename) throws IOException {
        String filePath = "path/to/your/videos/" + filename; // Adjust the path

        return response -> {
            HttpServletResponse httpResponse = response;
            httpResponse.setContentType("video/x-matroska");
            long fileSize = Files.size(Paths.get(filePath));
            httpResponse.setContentLengthLong(fileSize);

            // Open video using jmkvtoolnix
            MkvToolnix mkvToolnix = new MkvToolnix();
            MkvTrack videoTrack = mkvToolnix.getTrackInfo(filePath).getVideoTrack();

            try {
                // Implement chunked streaming logic based on byte range headers
                // ...

                // Read data in chunks for streaming
                InputStream inputStream = videoTrack.getStreamSource().open();
                int bufferSize = 8192; // Adjust as needed
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                // Handle exceptions and send error responses
            } finally {
                mkvToolnix.close();
            }
        };
    }
}

