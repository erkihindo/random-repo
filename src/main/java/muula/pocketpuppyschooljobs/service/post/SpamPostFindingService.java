package muula.pocketpuppyschooljobs.service.post;

import static java.util.Arrays.asList;
import static muula.pocketpuppyschooljobs.database.entity.enumerated.SystemPropertyKey.POST_SPAM_CHECK_MAX_ID;

import java.io.IOException;
import java.util.List;
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
public class SpamPostFindingService {

    private final SystemPropertyRepository systemPropertyRepository;
    private final PostRepository postRepository;

    List<String> spamPostBlurs = asList("UJ2*^]h}kVe.UbcYjGf+Y*iebFjZh~brbHfj");

    public void hidePossibleSpamPost() {
        Long maxProcessedId = getMaxId();

        List<Post> postToUpdateList = postRepository.findNextPost(maxProcessedId);

        if (postToUpdateList.isEmpty()) return;

        Post postToUpdate = postToUpdateList.iterator().next();

        try {
            process(postToUpdate);
            updatePointer(postToUpdateList);
            log.info("Checked post for spam: {}", postToUpdate.getId());
        } catch (IOException e) {
            log.info("Failed to check post, id: {}", postToUpdate.getId(), e);
        }
    }

    private Long getMaxId() {
        val optionalRow = systemPropertyRepository.findFirstByKeyString(POST_SPAM_CHECK_MAX_ID);

        if (!optionalRow.isPresent()) {
            return 0L;
        } else { return Long.valueOf(optionalRow.get().getValue()); }
    }

    private void process(Post postToUpdate) throws IOException {
        if (spamPostBlurs.contains(postToUpdate.getBlurHash())) {
            postToUpdate.setIsHidden(Boolean.TRUE);
            log.error("Found a possible Spam post: " + postToUpdate.getId());
            postRepository.save(postToUpdate);
        }
    }

    private void updatePointer(List<Post> postToUpdateList) {
        Long maxId = postToUpdateList.stream()
            .map(Post::getId)
            .max(Long::compare)
            .get();

        systemPropertyRepository.setValueByKey(POST_SPAM_CHECK_MAX_ID.name(), maxId.toString());
    }
}
