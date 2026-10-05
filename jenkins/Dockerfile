FROM jenkins/jenkins:lts-jdk21

USER root
RUN curl -fsSL "https://download.docker.com/linux/static/stable/x86_64/docker-29.8.2.tgz" -o /tmp/docker.tgz \
    && tar -xzf /tmp/docker.tgz -C /tmp docker/docker \
    && mv /tmp/docker/docker /usr/bin/docker \
    && chmod 755 /usr/bin/docker \
    && rm -rf /tmp/docker /tmp/docker.tgz
RUN jenkins-plugin-cli --plugins workflow-aggregator
