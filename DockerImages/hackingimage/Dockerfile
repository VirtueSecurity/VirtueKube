# syntax=docker/dockerfile:1.4
FROM debian:stable-slim

# Set environment variables for non-interactive installations
ENV DEBIAN_FRONTEND=noninteractive

# Update package lists and install essential tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential \
    git \
    wget \
    curl \
    nmap \    
    iputils-ping \
    unzip \
    lsb-release \
    net-tools \
    dnsutils \
    sudo \
    util-linux \
    jq \
    python3 \
    python3-pip \
    nano \
    less \
    procps \
    gnupg2 \
    python3-kubernetes \
    python3-docker \
    python3-pretty-yaml \
    python3-requests \
    python3-tabulate \
    jq \
    yq \
    ca-certificates && \    
    rm -rf /var/lib/apt/lists/*

# Install Python tools for Kubernetes/Docker Swarm penetration testing
# Note: Some tools might require specific Python versions or additional dependencies.
# This is a basic set; expand as needed.
# RUN apt search python
# RUN pip3 install --no-cache-dir \
#     kubernetes \
#     docker \
#     pyyaml \
#     requests \
#     tabulate

# Create a non-root user and group
ARG APP_USER=appuser
ARG APP_UID=1000
ARG APP_GID=1000

RUN groupadd -r -g ${APP_GID} ${APP_USER} && \    
    useradd -r -u ${APP_UID} -g ${APP_GID} -m -s /bin/bash ${APP_USER} && \
    echo "appuser ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers && cat /etc/sudoers


ARG TARGETARCH

# Install AWS CLI
# Use TARGETARCH to select the correct download URL
RUN case ${TARGETARCH} in \
    "amd64") AWS_CLI_ARCH="x86_64";; \
    "arm64") AWS_CLI_ARCH="aarch64";; \
    *) echo "Unsupported architecture: ${TARGETARCH}"; exit 1;; \
    esac && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-${AWS_CLI_ARCH}.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install -i /usr/local/aws-cli -b /usr/local/bin && \
    rm awscliv2.zip && \
    rm -rf aws

# Install kubectl
# Use TARGETARCH to select the correct download URL
RUN case ${TARGETARCH} in \
    "amd64") KUBECTL_ARCH="amd64";; \
    "arm64") KUBECTL_ARCH="arm64";; \
    *) echo "Unsupported architecture: ${TARGETARCH}"; exit 1;; \
    esac && \
    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/${KUBECTL_ARCH}/kubectl" && \
    chmod +x kubectl && \
    mv kubectl /usr/local/bin/

# Install Docker Client without the daemon
# Use TARGETARCH to select the correct download URL for static binaries
RUN case ${TARGETARCH} in \
    "amd64") DOCKER_ARCH="x86_64";; \
    "arm64") DOCKER_ARCH="aarch64";; \
    *) echo "Unsupported architecture: ${TARGETARCH}"; exit 1;; \
    esac && \
    # Check Docker static binary releases for the latest stable version compatible with multi-arch builds.
    # The version below (26.1.4) is an example, verify for current stability.
    curl -fsSL https://download.docker.com/linux/static/stable/${DOCKER_ARCH}/docker-26.1.4.tgz -o /tmp/docker.tgz && \
    tar --strip-components=1 -xvzf /tmp/docker.tgz -C /usr/local/bin/ docker/docker && \
    rm /tmp/docker.tgz


# Ensure permissions for the non-root user
# /usr/local/bin should already be globally executable, but it's good to be explicit.
# We also need to ensure that the AWS CLI installation directory is readable by the user.
RUN chmod -R go+rx /usr/local/bin/aws /usr/local/bin/kubectl /usr/local/bin/docker && \
    chmod -R go+rx /usr/local/aws-cli

# Set the working directory (optional, but good practice)
WORKDIR /home/${APP_USER}

# Switch to the non-root user
USER ${APP_USER}

# Clean up to reduce image size (optional but recommended) - perform this *before* switching user if possible for root-level cleanup
# But since we need apt-get, which requires root, it's typically done before the USER switch.
# We did initial cleanup, final cleanup won't run as non-root user.
# The initial cleanup `rm -rf /var/lib/apt/lists/*` handles most of it.

# Verify installations as the non-root user
RUN aws --version
RUN kubectl version --client
RUN docker --version

# Set a default command or entrypoint if needed
CMD ["bash"]