package muula.pocketpuppyschooljobs.service.post;

import static muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey.POST_THUMBNAIL_MAX_ID;

import io.trbl.blurhash.BlurHash;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import muula.pocketpuppyschooljobs.database.entity.Post;
import muula.pocketpuppyschooljobs.database.repository.PostRepository;
import muula.pocketpuppyschooljobs.database.repository.SystemPropertyRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostThumbnailService {

    private final SystemPropertyRepository systemPropertyRepository;
    private final PostRepository postRepository;

    public void tryAndUpdateThumbnail() {
        Long maxProcessedId = getMaxId();

        List<Post> postToUpdateList = postRepository.findNextPost(maxProcessedId);

        if (postToUpdateList.isEmpty()) return;

        Post postToUpdate = postToUpdateList.iterator().next();

        try {
            process(postToUpdate);
            updatePointer(postToUpdateList);
            log.info("Update post: {}", postToUpdate.getId());
        } catch (IOException e) {
            log.info("Failed to check post, id: {}", postToUpdate.getId(), e);
        }
    }

    private Long getMaxId() {
        val optionalRow = systemPropertyRepository.findFirstByKeyString(POST_THUMBNAIL_MAX_ID);

        if (!optionalRow.isPresent()) {
            return 0L;
        } else { return Long.valueOf(optionalRow.get().getValue()); }
    }

    private void process(Post postToUpdate) throws IOException {
        URL url = new URL(postToUpdate.getLink());
        BufferedImage image = ImageIO.read(url);
        int height = image.getHeight();
        int width = image.getWidth();

        String blurHash = BlurHash.encode(image);

        postToUpdate.setHeight((long) height);
        postToUpdate.setWidth((long) width);
        postToUpdate.setBlurHash(blurHash);

        postRepository.save(postToUpdate);
    }

    private void updatePointer(List<Post> postToUpdateList) {
        Long maxId = postToUpdateList.stream()
            .map(Post::getId)
            .max(Long::compare)
            .get();

        systemPropertyRepository.setValueByKey(POST_THUMBNAIL_MAX_ID.name(), maxId.toString());
    }
}
