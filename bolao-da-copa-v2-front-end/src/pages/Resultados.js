import React from 'react';
import { DataTable } from 'primereact/components/datatable/DataTable';
import { Column } from 'primereact/components/column/Column';
class Resultados extends React.Component {
    constructor() {
        super();
        this.state = {
            mostrarAjaxLoader: false,
            resultadoCalculado: false,
            resultados: null,
            campeao: '',
            vice: '',
            terceiro: '',
            quarto: '',
            mensagem: '', 
            mensagemClassName: ''
        }
    }

    mostrarAjaxLoader() { this.setState({ mostrarAjaxLoader: true }); }
    ocultarAjaxLoader() { this.setState({ mostrarAjaxLoader: false }); }
    mostrarErro(mensagem) { this.setState({ mensagem, mensagemClassName: 'alert alert-danger', }); }

    handleUserInput(e) {
        const nome = e.target.name;
        const valor = e.target.value;
        this.setState({
            [nome]: valor,
        });
    }

    async handleCacularClicado() {
        this.mostrarAjaxLoader();
        try {
            let resultado = {
                campeao: this.state.campeao, 
                vice : this.state.vice, 
                terceiro : this.state.terceiro, 
                quarto : this.state.quarto
            };
            const response = await fetch('http://localhost:8080/BolaoDaCopaV2_RS/webresources/resultados', {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    method: 'POST',
                    body: JSON.stringify(resultado),
            });
            let ranking = await response.json();
            this.setState({resultados : ranking, resultadoCalculado : true});
            console.log(this.state.resultados);
        } catch (e) {
            this.mostrarErro('Ocorreu um problema!');
            console.log(e);
        }
        this.ocultarAjaxLoader();
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                { this.state.mensagem !== '' &&
                    <div className={this.state.mensagemClassName}>
                        {this.state.mensagem}
                    </div>
                }
                    <div className="col-lg-12 text-center">
                        <h4>Cacular Resultados</h4>
                        <form className="form-horizontal" name="formResultado">

                            <div className="form-group">
                                <label className="col-sm-2 control-label" htmlFor="campeao">Campeao</label>
                                <div className="col-sm-4">
                                    <input type="text"
                                        className="form-control"
                                        name="campeao"
                                        label="Campeão"
                                        value={this.state.campeao}
                                        onChange={(event) => this.handleUserInput(event)} />
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="col-sm-2 control-label" htmlFor="vice">Vice</label>
                                <div className="col-sm-4">
                                    <input type="text"
                                        className="form-control"
                                        name="vice"
                                        label="Vice"
                                        value={this.state.vice}
                                        onChange={(event) => this.handleUserInput(event)} />
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="col-sm-2 control-label" htmlFor="terceiro">Terceiro</label>
                                <div className="col-sm-4">
                                    <input type="text"
                                        className="form-control"
                                        name="terceiro"
                                        label="Terceiro"
                                        value={this.state.terceiro}
                                        onChange={(event) => this.handleUserInput(event)} />
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="col-sm-2 control-label" htmlFor="quarto">Quarto</label>
                                <div className="col-sm-4">
                                    <input type="text"
                                        className="form-control"
                                        name="quarto"
                                        label="quarto"
                                        value={this.state.quarto}
                                        onChange={(event) => this.handleUserInput(event)} />
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-sm-12">
                                    <a className="btn btn-default"
                                        name="enviar"
                                        onClick={() => this.handleCacularClicado()}>
                                        Calcular
                                </a>
                                </div>
                            </div>
                        </form>
                    </div>
                    {this.state.mostrarAjaxLoader && (<div className='ajaxLoaderClass' />)}
                </div>

                {this.state.resultadoCalculado &&
                    <div className="content-section implementation">
                        <DataTable name="tabelaResultados" id="tabelaResultados" 
                        value={this.state.resultados} 
                        paginator={true} 
                        rows={10} style={{textAlign: 'center'}}>

                            <Column field="nome" header="Palpiteiro" />

                            <Column field="pontuacao" header="Pontos" />

                        </DataTable>
                    </div>
                }
            </div>


        )
    }
}

export default Resultados; 