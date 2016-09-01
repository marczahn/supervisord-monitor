function getColorClassesForStatus(status) {
    if (status == 'STOPPED') {
        return 'yellow darken-3';
    }
    if (status == 'FATAL') {
        return 'red darken-1';
    }
    if (status == 'RUNNING') {
        return 'green darken-1';
    }

    return '';
}

var NodeList = React.createClass({
    getInitialState: function() {
        return {nodes: []};
    },
    render: function() {
        return (
            <div className="node-list">
                {this.state.nodes.map(function(node, i) {
                    return <NodeItem node={node}/>
                })}
            </div>
        );
    },
    componentDidMount: function() {
        var self = this;
        var loadNodes = function() {
            fetch('/nodes', {
                method: 'get',
                cache: false
            }).then(function(response) {
                return response.json();
            }).then(function(nodes) {
                self.setState({
                    nodes: nodes
                });
            });
        };

        window.setInterval(loadNodes, 3000);
    }
});

var NodeItem = React.createClass({
    render: function() {
        var node = this.props.node;
        var stateClass = 'new badge ' + (node.state.name == 'RUNNING' ? 'green' : 'red');

        return (
            <section class="node">
                <h4 className="header">{node.name}<span className={stateClass} data-badge-caption={node.state.name}/></h4>
                <table className="processes responsive-table bordered">
                    <thead>
                        <tr>
                            <th>Process</th>
                            <th>PID</th>
                            <th>Up since</th>
                            <th>Down since</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {node.processes.map(function(process, i) {
                            var stateClass = getColorClassesForStatus(process.state.name);

                            var started = (new Date(process.start * 1000)).toUTCString();
                            var stopped = '-';
                            if (process.stop != 0) {
                                started = '-';
                                stopped = (new Date(process.stop * 1000)).toUTCString();
                            }

                            return (
                                <tr className={stateClass}>
                                    <td>{process.name}</td>
                                    <td>{process.pid || "-"}</td>
                                    <td>{started}</td>
                                    <td>{stopped}</td>
                                    <td>{process.state.name}</td>
                                </tr>
                             )
                         })}
                    </tbody>
                </table>
            </section>
        );
    }
});

ReactDOM.render(
    <NodeList />,
    document.getElementById('content')
);