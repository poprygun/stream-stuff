#!/usr/bin/env bash

function set_bash_fail_on_error() {
    set -o errexit
    set -o errtrace
    set -o nounset
    set -o pipefail
}

function create_target_directory() {
    mkdir -p target
}

function go_to_kafka_stresser_target_directory() {
    root_directory=$(git rev-parse --show-toplevel)

    cd "$root_directory/kafka-stresser/target"
}

function get_gatling_bundle() {
    go_to_kafka_stresser_target_directory

    curl -O https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.3.0/gatling-charts-highcharts-bundle-3.3.0-bundle.zip
    unzip gatling-charts-highcharts-bundle-3.3.0-bundle.zip
}

function build_gatling_kafka() {
    go_to_kafka_stresser_target_directory

    cd ../gatling-kafka
    sbt clean assembly
}

function install_gatling_kafka_to_gatling_bundle() {
    go_to_kafka_stresser_target_directory

    cp ../gatling-kafka/target/scala*/gatling-kafka-assembly*.jar gatling-charts-highcharts-bundle-3.3.0/lib
}

function main() {
    set_bash_fail_on_error
    create_target_directory
    get_gatling_bundle
    build_gatling_kafka
    install_gatling_kafka_to_gatling_bundle
}

main
