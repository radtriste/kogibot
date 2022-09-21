package org.kiegroup.kogibot.listeners.pr.impl;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.kiegroup.kogibot.config.KogibotConfiguration;
import org.kiegroup.kogibot.config.Labels;
import org.kiegroup.kogibot.listeners.pr.PRConfigListener;
import org.kiegroup.kogibot.util.Constants;
import org.kiegroup.kogibot.util.Constants.DefaultLabels;
import org.kiegroup.kogibot.util.MatchingPathsValuesUtils;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import io.quarkiverse.githubapp.ConfigFile;
import io.quarkiverse.githubapp.event.PullRequest;

public class PRLabelsListener implements PRConfigListener {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public void onOpenedPullRequest(
            @PullRequest.Opened @PullRequest.Reopened @PullRequest.Synchronize GHEventPayload.PullRequest prPayLoad,
            @ConfigFile(Constants.KOGIBOT_CONFIG_FILE) Optional<KogibotConfiguration> kogibotConfiguration,
            GitHub github) throws IOException {
        applyForConfig(prPayLoad, kogibotConfiguration, github);
    }

    @Override
    public String getListenerName() {
        return "Labels";
    }

    @Override
    public boolean hasConfigEnabled(KogibotConfiguration kogibotConfiguration) {
        return kogibotConfiguration.getLabels() != null;
    }

    @Override
    public void apply(GHEventPayload.PullRequest prPayLoad, KogibotConfiguration kogibotConfiguration, GitHub github)
            throws IOException {
        List<String> labels = retrieveLabels(prPayLoad.getPullRequest(), kogibotConfiguration.getLabels());

        GHRepository ghRepository = prPayLoad.getRepository();
        List<GHLabel> ghLabels = ghRepository.listLabels().toList();
        for (String labelName : labels) {
            if (!ghLabels.stream().map(GHLabel::getName).anyMatch(l -> l.equals(labelName))) {
                LOG.debugf("Label [%s] not found, creating...", labelName);

                String color = null;
                String description = null;
                if (DefaultLabels.hasLabel(labelName)) {
                    color = DefaultLabels.getLabel(labelName).getColor();
                    description = DefaultLabels.getLabel(labelName).getDescription();
                }
                LOG.infof("Create label %s with color %s and description %s", labelName, color, description);
                ghRepository.createLabel(labelName, color, description);
            }

            // Add label to PR
            LOG.debugf("Applying label [%s].", labelName);
            prPayLoad.getPullRequest().addLabels(labelName);
        }
    }

    public List<String> retrieveLabels(GHPullRequest pullRequest, Labels labelsCfg) throws IOException {
        return MatchingPathsValuesUtils.retrieveMatchingPathsValuesFromPullRequest(pullRequest, labelsCfg);
    }
}