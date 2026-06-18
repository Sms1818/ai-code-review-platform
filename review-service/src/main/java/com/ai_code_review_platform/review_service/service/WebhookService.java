package com.ai_code_review_platform.review_service.service;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.dto.PullRequestEventMessage;
import com.ai_code_review_platform.review_service.entity.PullRequestEvent;
import com.ai_code_review_platform.review_service.producer.ReviewEventProducer;
import com.ai_code_review_platform.review_service.repository.PullRequestEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final PullRequestEventRepository repository;
    private final ReviewEventProducer producer;

    public void processPullRequestEvent(
            BitbucketWebhookRequest request) {

        if (request.getPullrequest() == null) {

            log.warn(
                    "Pull Request data not found in webhook payload");

            return;
        }

        String workspace = extractWorkspace(request);
        String repositorySlug = extractRepositorySlug(request);
        String cloneUrl = extractCloneUrl(request, workspace, repositorySlug);

        PullRequestEvent event = PullRequestEvent.builder()
                .eventKey("pullrequest:created")
                .repositoryName(repositorySlug)
                .pullRequestId(
                        request.getPullrequest()
                                .getId())
                .title(
                        request.getPullrequest()
                                .getTitle())
                .author(
                        request.getActor()
                                .getDisplay_name())
                .sourceBranch(
                        request.getPullrequest()
                                .getSource()
                                .getBranch()
                                .getName())
                .targetBranch(
                        request.getPullrequest()
                                .getDestination()
                                .getBranch()
                                .getName())
                .status("RECEIVED")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(event);

        PullRequestEventMessage message = PullRequestEventMessage.builder()
                .pullRequestId(
                        request.getPullrequest()
                                .getId())
                .title(
                        request.getPullrequest()
                                .getTitle())
                .repositoryName(repositorySlug)
                .workspace(workspace)
                .repositorySlug(repositorySlug)
                .sourceBranch(
                        request.getPullrequest()
                                .getSource()
                                .getBranch()
                                .getName())
                .targetBranch(
                        request.getPullrequest()
                                .getDestination()
                                .getBranch()
                                .getName())
                .author(
                        request.getActor()
                                .getDisplay_name())
                .cloneUrl(cloneUrl)
                .build();

        producer.publishReviewEvent(message);

        log.info(
                "Pull Request Event Saved Successfully: {}",
                event.getId());
    }

    private String extractWorkspace(BitbucketWebhookRequest request) {
        String fullName = request.getRepository().getFull_name();
        if (fullName != null && fullName.contains("/")) {
            return fullName.split("/", 2)[0];
        }
        return null;
    }

    private String extractRepositorySlug(BitbucketWebhookRequest request) {
        String fullName = request.getRepository().getFull_name();
        if (fullName != null && fullName.contains("/")) {
            return fullName.split("/", 2)[1];
        }
        return request.getRepository().getName();
    }

    private String extractCloneUrl(
            BitbucketWebhookRequest request,
            String workspace,
            String repositorySlug) {

        BitbucketWebhookRequest.Links links = request.getRepository().getLinks();
        if (links != null && links.getClone() != null) {
            List<BitbucketWebhookRequest.CloneLink> cloneLinks = links.getClone();

            String sshUrl = cloneLinks.stream()
                    .filter(link -> "ssh".equalsIgnoreCase(link.getName()))
                    .map(BitbucketWebhookRequest.CloneLink::getHref)
                    .findFirst()
                    .orElse(null);

            if (sshUrl != null) {
                return sshUrl;
            }

            String httpsUrl = cloneLinks.stream()
                    .filter(link -> "https".equalsIgnoreCase(link.getName()))
                    .map(BitbucketWebhookRequest.CloneLink::getHref)
                    .findFirst()
                    .orElse(null);

            if (httpsUrl != null) {
                return httpsUrl;
            }

            if (!cloneLinks.isEmpty()) {
                return cloneLinks.get(0).getHref();
            }
        }

        if (workspace != null && repositorySlug != null) {
            return "git@bitbucket.org:" + workspace + "/" + repositorySlug + ".git";
        }

        log.warn("Could not determine clone URL from webhook payload");
        return null;
    }
}
